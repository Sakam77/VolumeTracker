package com.volumetracker.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.volumetracker.app.data.db.entity.AlertHistoryEntity
import com.volumetracker.app.data.repository.AlertRepository
import com.volumetracker.app.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val alertRepository: AlertRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    init {
        loadAlerts()
    }
    
    private fun loadAlerts() {
        viewModelScope.launch {
            alertRepository.getAlertHistoryFlow()
                .catch { e ->
                    Timber.e(e, "Error loading alerts")
                    _uiState.value = DashboardUiState.Error("Failed to load alerts")
                }
                .collect { alerts ->
                    if (alerts.isEmpty()) {
                        _uiState.value = DashboardUiState.Empty
                    } else {
                        _uiState.value = DashboardUiState.Success(alerts)
                    }
                }
        }
    }
    
    fun refresh() {
        loadAlerts()
    }
}

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    object Empty : DashboardUiState()
    data class Success(val alerts: List<AlertHistoryEntity>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}
