package com.mohanjp.runningtracker.feature.runtrack.presentation.setup

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.utils.isAndroid33OrAbove
import com.mohanjp.runningtracker.common.utils.permission.helper.PermissionHandler
import com.mohanjp.runningtracker.common.utils.permission.helper.PermissionResult
import com.mohanjp.runningtracker.common.utils.permission.notification.NotificationPermission
import com.mohanjp.runningtracker.common.utils.presentation.setOnDebounceClickListener
import com.mohanjp.runningtracker.databinding.FragmentSetupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class SetupFragment : Fragment(R.layout.fragment_setup) {

    private val viewModel: SetupViewModel by viewModels()

    private val notificationPermissionHandler: PermissionHandler by lazy {
        NotificationPermission(
            fragment         = this,
            permissionResult = ::permissionResult
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSetupBinding.bind(view)

        binding.bindState(
            uiState  = viewModel.uiState,
            uiAction = viewModel.accept,
            uiEvent  = viewModel.uiEvent
        )
    }

    private fun FragmentSetupBinding.bindState(
        uiState: StateFlow<SetupUiState>,
        uiAction: (SetupUiAction) -> Unit,
        uiEvent: SharedFlow<SetupUiEvent>
    ) {
        checkNotificationPermission()

        uiEvent.onEach { event ->
            when(event) {
                SetupUiEvent.NavigateToRunScreen -> {
                    navigateToRunFragment()
                }
            }
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)

        bindInput(uiAction)

        bindClick(uiAction)
    }

    private fun FragmentSetupBinding.bindInput(uiAction: (SetupUiAction) -> Unit) {
        etName.doAfterTextChanged {
            uiAction(SetupUiAction.TypingName(it.toString().trim()))
        }
        etWeight.doAfterTextChanged {
            uiAction(SetupUiAction.TypingWeight(it.toString().trim()))
        }
    }

    private fun FragmentSetupBinding.bindClick(uiAction: (SetupUiAction) -> Unit) {
        tvContinue.setOnDebounceClickListener {
            uiAction(SetupUiAction.ContinueButtonClicked)
        }
    }

    private fun navigateToRunFragment() {
        findNavController().navigate(R.id.action_setupFragment_to_runFragment)
    }

    private fun checkNotificationPermission() {
        if(isAndroid33OrAbove()) {
            viewLifecycleOwner.lifecycle.addObserver(notificationPermissionHandler)
            notificationPermissionHandler.onViewCreated()
            notificationPermissionHandler.requestPermission()
        }
    }

    private fun permissionResult(permissionResult: PermissionResult) {
        when(permissionResult) {
           PermissionResult.PermissionGranted -> Timber.d("notification permission granted")
           is PermissionResult.Error -> Timber.d("notification permission denied: ${permissionResult.error}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(notificationPermissionHandler)
    }
}