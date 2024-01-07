package com.mohanjp.runningtracker.feature.runtrack.presentation.run

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(

): ViewModel() {

    private val _uiEvent = MutableSharedFlow<RunUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept: (RunUiAction) -> Unit

    init {
        accept = ::onUiAction
    }

    private fun onUiAction(action: RunUiAction) {
        when(action) {
            RunUiAction.NewButtonClicked -> {
                sendEvent(RunUiEvent.NavigateToTrackingScreen)
            }
        }
    }

    private fun sendEvent(event: RunUiEvent) = viewModelScope.launch {
        _uiEvent.emit(event)
    }
}

sealed interface RunUiAction {
    data object NewButtonClicked: RunUiAction
}

sealed interface RunUiEvent {
    data object NavigateToTrackingScreen: RunUiEvent
}