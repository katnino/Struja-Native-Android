package com.noniboy.struja.ui.screens.meter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noniboy.struja.data.model.Bill
import com.noniboy.struja.data.model.Meter
import com.noniboy.struja.data.model.Reading
import com.noniboy.struja.data.repository.BillRepository
import com.noniboy.struja.data.repository.MeterRepository
import com.noniboy.struja.data.repository.ReadingRepository
import com.noniboy.struja.domain.outlook.MonthOutlook
import com.noniboy.struja.domain.outlook.MonthOutlookCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class MeterUiState(
    val meter: Meter? = null,
    val readings: List<Reading> = emptyList(),
    val bills: List<Bill> = emptyList(),
    val outlook: MonthOutlook? = null,
    val isLoading: Boolean = true,
    val showDeleteDialog: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MeterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val meterRepository: MeterRepository,
    private val readingRepository: ReadingRepository,
    private val billRepository: BillRepository
) : ViewModel() {

    private val meterId: String = savedStateHandle["meterId"] ?: ""

    private val _uiState = MutableStateFlow(MeterUiState())
    val uiState: StateFlow<MeterUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val meter = meterRepository.getById(meterId)
            _uiState.value = _uiState.value.copy(meter = meter)

            readingRepository.getByMeterId(meterId).collect { readings ->
                _uiState.value = _uiState.value.copy(readings = readings)
                updateOutlook(readings, meter)
            }
        }

        viewModelScope.launch {
            billRepository.getByMeterId(meterId).collect { bills ->
                _uiState.value = _uiState.value.copy(
                    bills = bills,
                    isLoading = false
                )
            }
        }
    }

    private fun updateOutlook(readings: List<Reading>, meter: Meter?) {
        if (meter == null || readings.isEmpty()) return

        val now = LocalDate.now()
        val outlook = MonthOutlookCalculator.calculate(
            readings = readings,
            year = now.year,
            month = now.monthValue,
            approvedKw = meter.approvedKw
        )
        _uiState.value = _uiState.value.copy(outlook = outlook)
    }

    fun showDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    fun deleteMeter() {
        viewModelScope.launch {
            meterRepository.deleteById(meterId)
            _uiState.value = _uiState.value.copy(showDeleteDialog = false)
        }
    }
}
