package com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.notification

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.PermissionDialog
import com.mohanjp.runningtracker.common.utils.presentation.openSettings

class NotificationPermanentDialog(context: Context) : PermissionDialog(context) {

    override val description: String = context.getString(R.string.notification_permanent_text)

    override val btnText: String = context.getString(R.string.go_to_settings)

    override val image: Int = R.drawable.ic_notification

    override fun buttonClicked() {
        dismiss()
        context.openSettings()
    }
}


