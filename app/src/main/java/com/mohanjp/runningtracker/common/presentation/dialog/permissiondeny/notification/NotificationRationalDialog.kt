package com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.notification

import android.content.Context
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.PermissionDialog

class NotificationRationalDialog(
    context: Context,
    private val okBtnClicked: (() -> Unit)
) : PermissionDialog(context) {

    override val description: String = context.getString(R.string.notification_rational_text)

    override val btnText: String = context.getString(R.string.ok)

    override val image: Int = R.drawable.ic_notification

    override fun buttonClicked() {
        dismiss()
        okBtnClicked.invoke()
    }
}


