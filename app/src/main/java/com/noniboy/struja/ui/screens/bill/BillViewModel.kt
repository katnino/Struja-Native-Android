package com.noniboy.struja.ui.screens.bill

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noniboy.struja.data.model.Bill
import com.noniboy.struja.data.model.Meter
import com.noniboy.struja.data.repository.BillRepository
import com.noniboy.struja.data.repository.MeterRepository
import com.noniboy.struja.pdf.PdfGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class BillUiState(
    val meter: Meter? = null,
    val bill: Bill? = null,
    val isLoading: Boolean = true,
    val isGeneratingPdf: Boolean = false,
    val error: String? = null,
    val savedPdfName: String? = null
)

@HiltViewModel
class BillViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val meterRepository: MeterRepository,
    private val billRepository: BillRepository,
    private val pdfGenerator: PdfGenerator
) : ViewModel() {

    private val meterId: String = savedStateHandle["meterId"] ?: ""
    private val billId: String = savedStateHandle["billId"] ?: ""

    private val _uiState = MutableStateFlow(BillUiState())
    val uiState: StateFlow<BillUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val meter = meterRepository.getById(meterId)
            val bill = billRepository.getById(billId)
            _uiState.value = _uiState.value.copy(
                meter = meter,
                bill = bill,
                isLoading = false
            )
        }
    }

    fun generatePdf() {
        val state = _uiState.value
        val bill = state.bill ?: return
        val meter = state.meter ?: return

        _uiState.value = state.copy(isGeneratingPdf = true)

        viewModelScope.launch {
            try {
                val cacheUri = pdfGenerator.generate(
                    context = context,
                    meterName = meter.name,
                    periodStart = bill.periodStart,
                    periodEnd = bill.periodEnd,
                    blocks = bill.blocks,
                    fileSuffix = billId.take(8),
                    billResult = com.noniboy.struja.domain.tariff.BillResult(
                        blocks = bill.blocks,
                        totalKwh = bill.consumptionKwh,
                        mjernoMjesto = bill.mjernoMjesto,
                        obracunskaSnaga = bill.obracunskaSnaga,
                        serviceFee = bill.mjernoMjesto,
                        totalEnergy = bill.energyCost,
                        transmissionBaseCost = bill.transmissionBaseCost,
                        totalTransmission = bill.totalTransmission,
                        distributionBaseCost = bill.distributionBaseCost,
                        totalDistribution = bill.totalDistribution,
                        totalOie = bill.oieCost,
                        subtotal = bill.subtotal,
                        vatAmount = bill.vatAmount,
                        total = bill.total,
                        consumptionKwh = bill.consumptionKwh,
                        isPartial = bill.isPartial
                    )
                )

                // Persist a copy to Downloads so "PREUZMI PDF" actually saves;
                // fall back to the cache file on API < 29 (no storage permission declared).
                val (shareUri, savedName) = persistToDownloads(cacheUri)
                    ?: (cacheUri to null)

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, shareUri)
                    putExtra(Intent.EXTRA_TITLE, "Struja račun")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                // NB: createChooser() returns a NEW intent without flags, so the
                // NEW_TASK flag must go on the chooser itself, not the inner intent.
                val chooser = Intent.createChooser(intent, "Sačuvaj ili podijeli PDF račun").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(chooser)

                _uiState.value = _uiState.value.copy(isGeneratingPdf = false, savedPdfName = savedName)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isGeneratingPdf = false,
                    error = "Greška pri generisanju PDF-a: ${e.message}"
                )
            }
        }
    }

    /**
     * Copies the cache PDF into Download/Struja via MediaStore (API 29+).
     * Returns (uri to share, display name) or null when not possible.
     */
    private fun persistToDownloads(cacheUri: Uri): Pair<Uri, String>? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return null
        return try {
            val fileName = cacheUri.lastPathSegment?.substringAfterLast('/')
                ?.takeIf { it.endsWith(".pdf") } ?: "Struja-racun.pdf"
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/Struja")
            }
            val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val destUri = context.contentResolver.insert(collection, values) ?: return null
            context.contentResolver.openInputStream(cacheUri)?.use { input ->
                context.contentResolver.openOutputStream(destUri)?.use { output ->
                    input.copyTo(output)
                } ?: return null
            } ?: return null
            destUri to fileName
        } catch (e: Exception) {
            null
        }
    }
}
