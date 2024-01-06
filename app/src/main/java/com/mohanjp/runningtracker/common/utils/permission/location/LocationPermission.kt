package com.mohanjp.runningtracker.common.utils.permission.location

import androidx.fragment.app.Fragment
import com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.location.LocationPermanentDialog
import com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.location.LocationRationalDialog
import com.mohanjp.runningtracker.common.utils.permission.helper.AppPermissionEnum
import com.mohanjp.runningtracker.common.utils.permission.helper.PermissionHandlerImpl
import com.mohanjp.runningtracker.common.utils.permission.helper.PermissionResult

class LocationPermission(
    private val fragment: Fragment,
    permissionResult: (PermissionResult) -> Unit
): PermissionHandlerImpl(
    fragment,
    permissionResult
) {

    override val permissions: List<AppPermissionEnum>
        = listOf(AppPermissionEnum.COARSE_LOCATION, AppPermissionEnum.FINE_LOCATION)

    override fun showRationalDialog() {
        LocationRationalDialog(
            context = fragment.requireContext(),
            okBtnClicked = ::requestPermission
        ).show()
    }

    override fun showPermanentlyDeniedDialog() {
        LocationPermanentDialog(fragment.requireContext()).show()
    }
}