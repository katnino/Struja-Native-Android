package com.noniboy.struja.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noniboy.struja.data.model.Meter
import com.noniboy.struja.data.repository.MeterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class HomeUiState(
    val meters: List<Meter> = emptyList(),
    val isLoading: Boolean = true,
    val showAddMeter: Boolean = false,
    val newMeterName: String = "",
    val newMeterApprovedKw: Double = 3.3,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val meterRepository: MeterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            meterRepository.getAll().collect { meters ->
                _uiState.value = _uiState.value.copy(
                    meters = meters,
                    isLoading = false
                )
            }
        }
    }

    fun showAddMeter() {
        _uiState.value = _uiState.value.copy(showAddMeter = true)
    }

    fun hideAddMeter() {
        _uiState.value = _uiState.value.copy(
            showAddMeter = false,
            newMeterName = "",
            newMeterApprovedKw = 3.3,
            error = null
        )
    }

    fun updateNewMeterName(name: String) {
        _uiState.value = _uiState.value.copy(newMeterName = name)
    }

    fun updateNewMeterApprovedKw(kw: Double) {
        _uiState.value = _uiState.value.copy(newMeterApprovedKw = kw)
    }

    fun saveMeter() {
        val state = _uiState.value
        if (state.newMeterName.isBlank()) {
            _uiState.value = state.copy(error = "Ime brojila je obavezno")
            return
        }

        viewModelScope.launch {
            val meter = Meter(
                id = UUID.randomUUID().toString(),
                name = state.newMeterName.trim(),
                approvedKw = state.newMeterApprovedKw,
                createdAt = java.time.Instant.now().toString()
            )
            meterRepository.insert(meter)
            _uiState.value = _uiState.value.copy(
                showAddMeter = false,
                newMeterName = "",
                newMeterApprovedKw = 3.3,
                error = null
            )
        }
    }
}
