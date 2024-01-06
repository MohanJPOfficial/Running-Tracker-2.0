package com.mohanjp.runningtracker.common.utils.permission.helper

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

abstract class PermissionHandlerImpl(
    private val fragment: Fragment,
    private val permissionResult: (PermissionResult) -> Unit
) : PermissionHandler {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated() {
        permissionLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            ::onPermissionResult
        )
    }

    override fun requestPermission() {
        try {
            val deniedPermissions = filterDeniedPermissions(permissions)

            if(deniedPermissions.isEmpty()) {
                permissionResult.invoke(PermissionResult.PermissionGranted)
            } else {
                permissionLauncher.launch(
                    deniedPermissions.map { it.value }.toTypedArray()
                )
            }

        } catch(e: Exception) {
            permissionResult.invoke(PermissionResult.Error(e.toString()))
        }
    }

    private fun onPermissionResult(permissionsMap: Map<String, Boolean>) {

        val isAllPermissionsGranted = permissionsMap.entries.all { it.value }
        val isNeedToShowRationaleDialog = permissions.any { fragment.shouldShowRequestPermissionRationale(it.value) }

        when {
            isAllPermissionsGranted -> permissionResult.invoke(PermissionResult.PermissionGranted)

            isNeedToShowRationaleDialog -> showRationalDialog()

            else -> showPermanentlyDeniedDialog()
        }
    }

    private fun filterDeniedPermissions(appPermissions: List<AppPermissionEnum>): List<AppPermissionEnum> {
        return appPermissions.filter { appPermission ->
            ActivityCompat.checkSelfPermission(fragment.requireContext(), appPermission.value) != PackageManager.PERMISSION_GRANTED
        }
    }

    abstract val permissions: List<AppPermissionEnum>

    abstract fun showRationalDialog()

    abstract fun showPermanentlyDeniedDialog()
}