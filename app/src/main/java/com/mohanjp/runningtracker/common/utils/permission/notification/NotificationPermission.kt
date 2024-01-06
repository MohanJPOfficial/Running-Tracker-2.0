package com.mohanjp.runningtracker.common.utils.permission.notification

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.notification.NotificationPermanentDialog
import com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.notification.NotificationRationalDialog
import com.mohanjp.runningtracker.common.utils.permission.helper.AppPermissionEnum
import com.mohanjp.runningtracker.common.utils.permission.helper.PermissionHandlerImpl
import com.mohanjp.runningtracker.common.utils.permission.helper.PermissionResult

class NotificationPermission(
    private val fragment: Fragment,
    permissionResult: (PermissionResult) -> Unit
): PermissionHandlerImpl(
    fragment,
    permissionResult
) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override val permissions: List<AppPermissionEnum>
        = listOf(AppPermissionEnum.NOTIFICATION)

    override fun showRationalDialog() {
        NotificationRationalDialog(
            context = fragment.requireContext(),
            okBtnClicked = ::requestPermission
        ).show()
    }

    override fun showPermanentlyDeniedDialog() {
        NotificationPermanentDialog(fragment.requireContext()).show()
    }
}