package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.utils.presentation.makeGone
import com.mohanjp.runningtracker.common.utils.presentation.makeVisible
import com.mohanjp.runningtracker.common.utils.presentation.setOnDebounceClickListener
import com.mohanjp.runningtracker.databinding.FragmentTrackingBinding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private lateinit var binding: FragmentTrackingBinding
    private val viewModel: TrackingViewModel by viewModels()

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync { googleMap ->
            map = googleMap

            val pathPointsFlow = viewModel.uiState.mapNotNull { it.pathPoints }.distinctUntilChanged()

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    pathPointsFlow.first().also { addAllPolyline(it) }
                }
            }

            binding.bindState(
                uiAction = viewModel.accept,
                uiState  = viewModel.uiState,
                uiEvent  = viewModel.uiEvent
            )
        }
    }

    private fun FragmentTrackingBinding.bindState(
        uiAction: (TrackingUiAction) -> Unit,
        uiState: StateFlow<TrackingUiState>,
        uiEvent: SharedFlow<TrackingUiEvent>
    ) {
        uiEvent.onEach { event ->
            when(event) {
                is TrackingUiEvent.SendCommandToService -> {
                    sendCommandToService(
                        action = event.action
                    )
                }
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        bindPolyLines(uiState)

        bindClick(uiAction)
    }

    private fun FragmentTrackingBinding.bindPolyLines(
        uiState: StateFlow<TrackingUiState>
    ) {
        val cameraPositionFlow = uiState.mapNotNull { it.cameraPosition }.distinctUntilChanged()
        val latestPolylineFlow = uiState.mapNotNull { it.preLastAndLastLatLng }.distinctUntilChanged()
        val trackingStateFlow  = uiState.map { it.trackingState }.distinctUntilChanged()
        val toggleButtonText   = uiState.mapNotNull { it.toggleButtonText }.distinctUntilChanged()
        val stopwatchTimerFlow = uiState.map { it.stopwatchTimer }.distinctUntilChanged()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    cameraPositionFlow.collectLatest {
                        Timber.d("camera line $it")
                        moveCameraToUser(it)
                    }
                }

                launch {
                    latestPolylineFlow.collectLatest {
                        addLatestPolyline(it)
                    }
                }

                launch {
                    trackingStateFlow.collectLatest { trackingState ->
                        if(trackingState == TrackingState.TRACKING) {
                            btnFinishRun.makeGone()
                        } else {
                            btnFinishRun.makeVisible()
                        }
                    }
                }

                launch {
                    toggleButtonText.collectLatest {
                        btnToggleRun.text = it.asString(requireContext())
                    }
                }

                launch {
                    stopwatchTimerFlow.collectLatest {
                        updateTimer(it)
                    }
                }
            }
        }
    }

    private fun FragmentTrackingBinding.bindClick(uiAction: (TrackingUiAction) -> Unit) {
        btnToggleRun.setOnDebounceClickListener {
            uiAction(TrackingUiAction.ButtonToggleRunClicked)
        }

        btnFinishRun.setOnDebounceClickListener {
            uiAction(TrackingUiAction.ButtonFinishRunClicked)
        }
    }

    private fun moveCameraToUser(latLng: LatLng) {
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                15f
            )
        )
    }

    private fun addAllPolyline(pathPoints: Polylines) {
        Timber.d("add all >> $pathPoints")
        pathPoints.forEach { polyline ->
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .addAll(polyline)

            Timber.d("isMapNull ${map == null}")
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline(preLastAndLastLatLng: PreLastAndLastLatLng) {
        val polylineOptions = PolylineOptions()
            .color(Color.RED)
            .width(8f)
            .add(preLastAndLastLatLng.preLastLatLng)
            .add(preLastAndLastLatLng.lastLatLng)

        map?.addPolyline(polylineOptions)
    }

    private fun updateTimer(stopwatchTimer: String) {
        binding.tvTimer.text = stopwatchTimer
    }

    private fun sendCommandToService(action: TrackingServiceActionEnum) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action.name
            requireContext().startService(it)
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(requireContext(), TrackingService::class.java).also {
            requireContext().bindService(it, viewModel.serviceConnection, Context.BIND_AUTO_CREATE)
        }

        binding.mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        Timber.d("service was unbounded")
        requireContext().unbindService(viewModel.serviceConnection)
        binding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }
}