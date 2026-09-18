package com.noniboy.struja.ui.screens.reading

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noniboy.struja.data.model.Bill
import com.noniboy.struja.data.model.Meter
import com.noniboy.struja.data.model.Reading
import com.noniboy.struja.data.repository.BillRepository
import com.noniboy.struja.data.repository.MeterRepository
import com.noniboy.struja.data.repository.ReadingRepository
import com.noniboy.struja.domain.tariff.BillResult
import com.noniboy.struja.domain.tariff.TariffCalculator
import com.noniboy.struja.domain.validation.ReadingValidator
import com.noniboy.struja.domain.validation.ReadingValidationException
import com.noniboy.struja.vision.ExtractResult
import com.noniboy.struja.vision.OcrEngine
import com.noniboy.struja.vision.VisionExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

data class ReadingUiState(
    val meter: Meter? = null,
    val vt: String = "",
    val mt: String = "",
    val recordedAt: String = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    val mode: String = "manual",
    val ocrEngine: OcrEngine = OcrEngine.GEMINI,
    val isSaving: Boolean = false,
    val isExtracting: Boolean = false,
    val previewBill: BillResult? = null,
    val error: String? = null,
    val success: Boolean = false,
    val pickedImageUri: Uri? = null,
    val extractedResult: ExtractResult? = null
)

@HiltViewModel
class ReadingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val meterRepository: MeterRepository,
    private val readingRepository: ReadingRepository,
    private val billRepository: BillRepository,
    private val visionExtractor: VisionExtractor
) : ViewModel() {

    private val meterId: String = savedStateHandle["meterId"] ?: ""

    private val _uiState = MutableStateFlow(ReadingUiState())
    val uiState: StateFlow<ReadingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val meter = meterRepository.getById(meterId)
            _uiState.value = _uiState.value.copy(
                meter = meter,
                ocrEngine = visionExtractor.getOcrEngine()
            )
        }
    }

    /** Re-read Settings-level engine (Settings can change while this screen is away). */
    fun refreshOcrEngine() {
        _uiState.value = _uiState.value.copy(ocrEngine = visionExtractor.getOcrEngine())
    }

    fun updateVt(vt: String) {
        _uiState.value = _uiState.value.copy(vt = vt)
        updatePreview()
    }

    fun updateMt(mt: String) {
        _uiState.value = _uiState.value.copy(mt = mt)
        updatePreview()
    }

    fun updateRecordedAt(date: String) {
        _uiState.value = _uiState.value.copy(recordedAt = date)
    }

    fun updateMode(mode: String) {
        _uiState.value = _uiState.value.copy(mode = mode, pickedImageUri = null, extractedResult = null)
    }

    fun onImagePicked(uri: Uri) {
        _uiState.value = _uiState.value.copy(pickedImageUri = uri, extractedResult = null)
        // Safest route: prefer the photo's own EXIF capture date, keep manual date otherwise.
        viewModelScope.launch {
            val photoDate = try {
                visionExtractor.extractPhotoDate(uri)
            } catch (e: Exception) {
                null
            }
            if (photoDate != null) {
                _uiState.value = _uiState.value.copy(
                    recordedAt = photoDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                )
            }
        }
    }

    fun extractReadingFromImage() {
        val uri = _uiState.value.pickedImageUri ?: return
        _uiState.value = _uiState.value.copy(isExtracting = true, error = null)

        viewModelScope.launch {
            try {
                val result = visionExtractor.extractFromUri(uri)
                val current = _uiState.value
                _uiState.value = current.copy(
                    isExtracting = false,
                    extractedResult = result,
                    vt = result.vt?.toString() ?: current.vt,
                    mt = result.mt?.toString() ?: current.mt
                )
                updatePreview()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExtracting = false,
                    error = "Greška pri ekstrakciji: ${e.message}"
                )
            }
        }
    }

    fun dismissExtractedResult() {
        _uiState.value = _uiState.value.copy(extractedResult = null)
    }

    private fun updatePreview() {
        val state = _uiState.value
        val vt = state.vt.toIntOrNull()
        val mt = state.mt.toIntOrNull()
        val meter = state.meter ?: return

        if (vt == null || mt == null || vt < 0 || mt < 0) {
            _uiState.value = state.copy(previewBill = null)
            return
        }

        viewModelScope.launch {
            val previousReading = readingRepository.getLatestByMeterId(meterId)

            if (previousReading != null) {
                val deltaVt = vt - (previousReading.vt ?: 0)
                val deltaMt = mt - (previousReading.mt ?: 0)

                if (deltaVt >= 0 && deltaMt >= 0) {
                    val daysInPeriod = try {
                        val prevDate = LocalDate.parse(previousReading.recordedAt)
                        val currentDate = LocalDate.parse(state.recordedAt)
                        java.time.temporal.ChronoUnit.DAYS.between(prevDate, currentDate).toInt()
                    } catch (e: Exception) {
                        null
                    }

                    val billResult = TariffCalculator.calculateBill(
                        vtKwh = deltaVt.toDouble(),
                        mtKwh = deltaMt.toDouble(),
                        approvedKw = meter.approvedKw,
                        daysInPeriod = daysInPeriod
                    )
                    _uiState.value = _uiState.value.copy(previewBill = billResult)
                }
            } else {
                // No previous reading: a bill is only created from the second
                // reading on, so don't preview absolute counter values as kWh.
                _uiState.value = _uiState.value.copy(previewBill = null)
            }
        }
    }

    fun saveReading() {
        val state = _uiState.value
        val vt = state.vt.toIntOrNull()
        val mt = state.mt.toIntOrNull()
        val meter = state.meter ?: return

        if (vt == null || mt == null) {
            _uiState.value = state.copy(error = "Unesite VT i MT vrijednosti")
            return
        }

        _uiState.value = state.copy(isSaving = true, error = null)

        viewModelScope.launch {
            try {
                val previousReading = readingRepository.getLatestByMeterId(meterId)

                ReadingValidator.validateDate(state.recordedAt, previousReading?.recordedAt)
                ReadingValidator.validateReadings(
                    vt, mt,
                    previousReading?.vt,
                    previousReading?.mt
                )

                val reading = Reading(
                    id = UUID.randomUUID().toString(),
                    meterId = meterId,
                    recordedAt = state.recordedAt,
                    vt = vt,
                    mt = mt,
                    source = state.mode,
                    confidence = state.extractedResult?.confidence,
                    createdAt = Instant.now().toString()
                )
                readingRepository.insert(reading)

                if (previousReading != null) {
                    val deltaVt = vt - (previousReading.vt ?: 0)
                    val deltaMt = mt - (previousReading.mt ?: 0)
                    val daysInPeriod = java.time.temporal.ChronoUnit.DAYS.between(
                        LocalDate.parse(previousReading.recordedAt),
                        LocalDate.parse(state.recordedAt)
                    ).toInt()

                    val billResult = TariffCalculator.calculateBill(
                        vtKwh = deltaVt.toDouble(),
                        mtKwh = deltaMt.toDouble(),
                        approvedKw = meter.approvedKw,
                        daysInPeriod = daysInPeriod
                    )

                    val bill = Bill(
                        id = UUID.randomUUID().toString(),
                        meterId = meterId,
                        periodStart = previousReading.recordedAt,
                        periodEnd = state.recordedAt,
                        prevReadingId = previousReading.id,
                        currReadingId = reading.id,
                        approvedKw = meter.approvedKw,
                        consumptionKwh = billResult.consumptionKwh,
                        mjernoMjesto = billResult.mjernoMjesto,
                        obracunskaSnaga = billResult.obracunskaSnaga,
                        energyCost = billResult.totalEnergy,
                        transmissionBaseCost = billResult.transmissionBaseCost,
                        totalTransmission = billResult.totalTransmission,
                        distributionBaseCost = billResult.distributionBaseCost,
                        totalDistribution = billResult.totalDistribution,
                        oieCost = billResult.totalOie,
                        subtotal = billResult.subtotal,
                        vatAmount = billResult.vatAmount,
                        total = billResult.total,
                        blocks = billResult.blocks,
                        isPartial = billResult.isPartial,
                        createdAt = Instant.now().toString()
                    )
                    billRepository.insert(bill)
                }

                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    success = true
                )
            } catch (e: ReadingValidationException) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Greška pri čuvanju: ${e.message}"
                )
            }
        }
    }
}
