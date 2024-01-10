package com.mohanjp.runningtracker.feature.runtrack.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohanjp.runningtracker.common.utils.presentation.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SetupUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept: (SetupUiAction) -> Unit

    init {
        accept = ::onUiAction
    }

    private fun onUiAction(action: SetupUiAction) {
        when(action) {
            is SetupUiAction.TypingName -> {
                _uiState.update {
                    it.copy(
                        typedName = action.name
                    )
                }
            }
            is SetupUiAction.TypingWeight -> {
                _uiState.update {
                    it.copy(
                        typedWeight = action.weight
                    )
                }
            }
            SetupUiAction.ContinueButtonClicked -> {
                validateInternal()
            }
        }
    }

    private fun validateInternal() {
        /**
         * TODO
         */
        sendEvent(SetupUiEvent.NavigateToRunScreen)
    }

    private fun sendEvent(event: SetupUiEvent) = viewModelScope.launch {
        _uiEvent.emit(event)
    }
}

sealed interface SetupUiAction {
    data class TypingName(val name: String): SetupUiAction
    data class TypingWeight(val weight: String): SetupUiAction
    data object ContinueButtonClicked: SetupUiAction
}

sealed class SetupUiEvent {
    data object NavigateToRunScreen : SetupUiEvent()
}

data class SetupUiState(
    val typedName: String = "",
    val typedWeight: String = "",
    val error: UiText? = null
)