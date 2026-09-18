package com.noniboy.struja.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noniboy.struja.data.backup.BackupEnvelope
import com.noniboy.struja.data.backup.BackupException
import com.noniboy.struja.data.backup.BackupRepository
import com.noniboy.struja.data.backup.ImportSummary
import com.noniboy.struja.data.backup.parseBackup
import com.google.gson.Gson
import com.noniboy.struja.data.backup.summary
import com.noniboy.struja.vision.OcrEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class SettingsUiState(
    val apiKey: String = "",
    val ocrEngine: OcrEngine = OcrEngine.GEMINI,
    val showSettingsDialog: Boolean = false,
    val backupBusy: Boolean = false,
    val backupMessage: String? = null,
    val exportFileName: String? = null,
    val pendingImport: ImportSummary? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val backupRepository: BackupRepository,
    private val gson: Gson
) : ViewModel() {

    private val prefs = context.getSharedPreferences("struja_settings", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        val savedKey = prefs.getString("gemini_api_key", "") ?: ""
        _uiState.value = _uiState.value.copy(apiKey = savedKey, ocrEngine = OcrEngine.read(context))
    }

    fun showSettings() {
        _uiState.value = _uiState.value.copy(showSettingsDialog = true)
    }

    fun hideSettings() {
        _uiState.value = _uiState.value.copy(showSettingsDialog = false)
    }

    fun updateApiKey(key: String) {
        _uiState.value = _uiState.value.copy(apiKey = key)
    }

    fun saveApiKey() {
        prefs.edit().putString("gemini_api_key", _uiState.value.apiKey).apply()
        _uiState.value = _uiState.value.copy(showSettingsDialog = false)
    }

    fun getApiKey(): String {
        return _uiState.value.apiKey
    }

    /**
     * Settings-level OCR choice. Saved immediately; strictly no auto-fallback —
     * LOCAL never triggers the Gemini path elsewhere (see VisionExtractor).
     */
    fun updateOcrEngine(engine: OcrEngine) {
        OcrEngine.save(context, engine)
        _uiState.value = _uiState.value.copy(ocrEngine = engine)
    }

    // ---- Backup (meters + readings + bills, no API keys) ----

    private var pendingExportJson: String? = null
    private var pendingEnvelope: BackupEnvelope? = null

    fun onExportClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(backupBusy = true, backupMessage = null)
            try {
                pendingExportJson = withContext(Dispatchers.IO) { backupRepository.exportJson() }
                val fileName = "Struja-backup-" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".json"
                _uiState.value = _uiState.value.copy(backupBusy = false, exportFileName = fileName)
            } catch (e: Exception) {
                pendingExportJson = null
                _uiState.value = _uiState.value.copy(
                    backupBusy = false,
                    backupMessage = "Greška pri izvozu: ${e.message}"
                )
            }
        }
    }

    fun onExportFilePicked(uri: android.net.Uri) {
        val json = pendingExportJson ?: return
        pendingExportJson = null
        _uiState.value = _uiState.value.copy(exportFileName = null, backupBusy = true)
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(uri)?.use { out ->
                        out.write(json.toByteArray(Charsets.UTF_8))
                    } ?: throw Exception("Nije moguće otvoriti datoteku")
                }
                _uiState.value = _uiState.value.copy(
                    backupBusy = false,
                    backupMessage = "✓ Kopija izvezena"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    backupBusy = false,
                    backupMessage = "Greška pri izvozu: ${e.message}"
                )
            }
        }
    }

    fun onExportCancelled() {
        pendingExportJson = null
        _uiState.value = _uiState.value.copy(exportFileName = null, backupBusy = false)
    }

    fun onImportFilePicked(uri: android.net.Uri) {
        _uiState.value = _uiState.value.copy(backupBusy = true, backupMessage = null)
        viewModelScope.launch {
            try {
                val json = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        input.readBytes().toString(Charsets.UTF_8)
                    } ?: throw Exception("Nije moguće otvoriti datoteku")
                }
                val envelope = parseBackup(json, gson)
                pendingEnvelope = envelope
                _uiState.value = _uiState.value.copy(
                    backupBusy = false,
                    pendingImport = envelope.summary()
                )
            } catch (e: BackupException) {
                pendingEnvelope = null
                _uiState.value = _uiState.value.copy(
                    backupBusy = false,
                    backupMessage = e.message
                )
            } catch (e: Exception) {
                pendingEnvelope = null
                _uiState.value = _uiState.value.copy(
                    backupBusy = false,
                    backupMessage = "Greška pri uvozu: ${e.message}"
                )
            }
        }
    }

    fun confirmImport() {
        val envelope = pendingEnvelope ?: return
        pendingEnvelope = null
        _uiState.value = _uiState.value.copy(pendingImport = null, backupBusy = true)
        viewModelScope.launch {
            try {
                val summary = backupRepository.importEnvelope(envelope)
                _uiState.value = _uiState.value.copy(
                    backupBusy = false,
                    backupMessage = "✓ Uvezeno: ${summary.meters} brojila, " +
                        "${summary.readings} očitanja, ${summary.bills} računa"
                )
            } catch (e: BackupException) {
                _uiState.value = _uiState.value.copy(
                    backupBusy = false,
                    backupMessage = e.message
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    backupBusy = false,
                    backupMessage = "Greška pri uvozu: ${e.message}"
                )
            }
        }
    }

    fun cancelImport() {
        pendingEnvelope = null
        _uiState.value = _uiState.value.copy(pendingImport = null)
    }

    fun dismissBackupMessage() {
        _uiState.value = _uiState.value.copy(backupMessage = null)
    }
}
