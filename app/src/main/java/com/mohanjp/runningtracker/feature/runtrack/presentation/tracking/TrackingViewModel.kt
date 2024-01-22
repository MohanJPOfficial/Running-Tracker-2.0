package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.utils.presentation.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TrackingUiState()
    )

    private val _uiEvent = MutableSharedFlow<TrackingUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val accept: (TrackingUiAction) -> Unit

    val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TrackingService.TrackingServiceBinder
            val trackingService = binder.getBoundService()

            trackingService.isTracking.onEach { isTracking ->
                updateTrackingState(isTracking)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Timber.d("Service is disconnected")
        }
    }


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

    private fun updateTrackingState(isTracking: Boolean) {
        _uiState.update {
            if(isTracking) {
                it.copy(
                    trackingState = TrackingState.TRACKING,
                    toggleButtonText = UiText.StringResource(R.string.stop)
                )
            } else {
                it.copy(
                    trackingState = TrackingState.NOT_TRACKING,
                    toggleButtonText = UiText.StringResource(R.string.start)
                )
            }
        }
    }

    private fun updateCameraPosition(pathPoints: Polylines) {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            _uiState.update {
                it.copy(
                    cameraPosition = pathPoints.last().last()
                )
            }
        }
    }

    private fun updateLatestPolyline(pathPoints: Polylines) {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()

            _uiState.update {
                it.copy(
                    preLastAndLastLatLng = PreLastAndLastLatLng(
                        preLastLatLng = preLastLatLng,
                        lastLatLng = lastLatLng
                    )
                )
            }
        }
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
    val trackingState: TrackingState = TrackingState.NOT_TRACKING,
    val trackingServiceAction: TrackingServiceActionEnum = TrackingServiceActionEnum.ACTION_STOP_SERVICE,
    val toggleButtonText: UiText? = null,
    val cameraPosition: LatLng? = null,
    val preLastAndLastLatLng: PreLastAndLastLatLng? = null
)

data class PreLastAndLastLatLng(
    val preLastLatLng: LatLng,
    val lastLatLng: LatLng
)

enum class TrackingState {
    TRACKING,
    NOT_TRACKING
}

enum class TrackingServiceActionEnum {
    ACTION_START_OR_RESUME_SERVICE,
    ACTION_PAUSE_SERVICE,
    ACTION_STOP_SERVICE
}