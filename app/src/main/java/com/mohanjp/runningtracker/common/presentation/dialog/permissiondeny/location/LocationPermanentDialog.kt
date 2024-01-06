package com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.location

import android.content.Context
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.PermissionDialog
import com.mohanjp.runningtracker.common.utils.presentation.openSettings

class LocationPermanentDialog(context: Context) : PermissionDialog(context) {

    override val description: String = context.getString(R.string.location_permanent_text)

    override val btnText: String = context.getString(R.string.go_to_settings)

    override val image: Int = R.drawable.baseline_add_location_24

    override fun buttonClicked() {
        dismiss()
        context.openSettings()
    }
}


