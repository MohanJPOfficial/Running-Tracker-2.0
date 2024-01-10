package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<TrackingUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept: (TrackingUiAction) -> Unit

    init {
        accept = ::onUiAction
    }

    private fun onUiAction(action: TrackingUiAction) {
        when(action) {
            TrackingUiAction.ButtonToggleRunClicked -> {
                sendEvent(TrackingUiEvent.StartOrResumeService)
            }
            TrackingUiAction.ButtonFinishRunClicked -> {
            }
        }
    }

    private fun sendEvent(event: TrackingUiEvent) = viewModelScope.launch {
        _uiEvent.emit(event)
    }
}

sealed interface TrackingUiAction {
    data object ButtonToggleRunClicked: TrackingUiAction
    data object ButtonFinishRunClicked: TrackingUiAction
}

sealed interface TrackingUiEvent {
    data object StartOrResumeService: TrackingUiEvent
}

data class TrackingUiState(
    val nothing: String = ""
)