package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

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
import com.google.android.gms.maps.model.PolylineOptions
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.utils.presentation.makeGone
import com.mohanjp.runningtracker.common.utils.presentation.makeVisible
import com.mohanjp.runningtracker.common.utils.presentation.setOnDebounceClickListener
import com.mohanjp.runningtracker.databinding.FragmentTrackingBinding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private lateinit var binding: FragmentTrackingBinding
    private val viewModel: TrackingViewModel by viewModels()

    private var map: GoogleMap? = null

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            map = it

            addAllPolyline()
        }

        binding.bindState(
            uiAction = viewModel.accept,
            uiState  = viewModel.uiState,
            uiEvent  = viewModel.uiEvent
        )

        subscribeToObservers()
    }

    private fun FragmentTrackingBinding.bindState(
        uiAction: (TrackingUiAction) -> Unit,
        uiState: StateFlow<TrackingUiState>,
        uiEvent: SharedFlow<TrackingUiEvent>
    ) {
        uiEvent.onEach { event ->
            when(event) {
                TrackingUiEvent.StartOrResumeService -> {
                    /*sendCommandToService(
                        action = TrackingServiceState.ACTION_START_OR_RESUME_SERVICE
                    )*/

                    toggleRun()
                }
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        bindClick(uiAction)
    }

    private fun FragmentTrackingBinding.bindClick(uiAction: (TrackingUiAction) -> Unit) {
        btnToggleRun.setOnDebounceClickListener {
            uiAction(TrackingUiAction.ButtonToggleRunClicked)
        }

        btnFinishRun.setOnDebounceClickListener {
            uiAction(TrackingUiAction.ButtonFinishRunClicked)
        }
    }

    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    TrackingService.isTracking.collectLatest {
                        Timber.d("is Tracking >> $it")
                        updateTracking(it)
                    }
                }

                launch {
                    TrackingService.pathPointsFlow.collectLatest {
                        Timber.d("path points >> $it")
                        pathPoints = it
                        addLatestPolyline()
                        moveCameraToUser()
                    }
                }
            }
        }
    }

    private fun toggleRun() {
        if(isTracking) {
            sendCommandToService(TrackingServiceState.ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(TrackingServiceState.ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking

        if(!isTracking) {
            binding.apply {
                btnToggleRun.text = "Start"
                btnFinishRun.makeVisible()
            }
        } else {
            binding.apply {
                btnToggleRun.text = "Stop"
                btnFinishRun.makeGone()
            }
        }
    }

    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    15f
                )
            )
        }
    }

    private fun addAllPolyline() {
        pathPoints.forEach { polyline ->
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .addAll(polyline)

            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun sendCommandToService(action: TrackingServiceState) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action.name
            requireContext().startService(it)
        }
    }

    override fun onStart() {
        super.onStart()
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