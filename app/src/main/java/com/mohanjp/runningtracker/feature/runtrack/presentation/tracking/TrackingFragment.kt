package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.utils.presentation.setOnDebounceClickListener
import com.mohanjp.runningtracker.databinding.FragmentTrackingBinding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private lateinit var binding: FragmentTrackingBinding
    private val viewModel: TrackingViewModel by viewModels()

    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTrackingBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync { map = it }

        binding.bindState(
            uiAction = viewModel.accept,
            uiState  = viewModel.uiState,
            uiEvent  = viewModel.uiEvent
        )
    }

    private fun FragmentTrackingBinding.bindState(
        uiAction: (TrackingUiAction) -> Unit,
        uiState: StateFlow<TrackingUiState>,
        uiEvent: SharedFlow<TrackingUiEvent>
    ) {
        uiEvent.onEach { event ->
            when(event) {
                TrackingUiEvent.StartOrResumeService -> {
                    sendCommandToService(
                        action = TrackingServiceState.ACTION_START_OR_RESUME_SERVICE
                    )
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
