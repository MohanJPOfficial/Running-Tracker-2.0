package com.mohanjp.runningtracker.feature.runtrack.presentation.run

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.utils.permission.helper.PermissionHandler
import com.mohanjp.runningtracker.common.utils.permission.helper.PermissionResult
import com.mohanjp.runningtracker.common.utils.permission.location.LocationPermission
import com.mohanjp.runningtracker.common.utils.presentation.setOnDebounceClickListener
import com.mohanjp.runningtracker.databinding.FragmentRunBinding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class RunFragment : Fragment(R.layout.fragment_run) {

    private val viewModel: RunViewModel by viewModels()

    private val locationPermissionHandler: PermissionHandler by lazy {
        LocationPermission(
            fragment = this,
            permissionResult = ::locationPermissionResult
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRunBinding.bind(view)

        binding.bindState(
            uiAction = viewModel.accept,
            uiEvent  = viewModel.uiEvent
        )
    }

    private fun FragmentRunBinding.bindState(
        uiAction: (RunUiAction) -> Unit,
        uiEvent: SharedFlow<RunUiEvent>
    ) {
        checkLocationPermission()

        uiEvent.onEach { event ->
            when(event) {
                RunUiEvent.NavigateToTrackingScreen -> {
                    navigateToTrackingFragment()
                }
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        bindClick(uiAction)
    }

    private fun FragmentRunBinding.bindClick(uiAction: (RunUiAction) -> Unit) {
        fab.setOnDebounceClickListener {
            uiAction(RunUiAction.NewButtonClicked)
        }
    }

    private fun navigateToTrackingFragment() {
        findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
    }

    private fun checkLocationPermission() {
        viewLifecycleOwner.lifecycle.addObserver(locationPermissionHandler)
        locationPermissionHandler.onViewCreated()
        locationPermissionHandler.requestPermission()
    }

    private fun locationPermissionResult(permissionResult: PermissionResult) {
        when(permissionResult) {
            PermissionResult.PermissionGranted -> Timber.d("location permission granted")
            is PermissionResult.Error -> Timber.d("location permission denied: ${permissionResult.error}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(locationPermissionHandler)
    }
}
