package com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.location

import android.content.Context
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.presentation.dialog.permissiondeny.PermissionDialog

class LocationRationalDialog(
    context: Context,
    private val okBtnClicked: (() -> Unit)
) : PermissionDialog(context) {

    override val description: String = context.getString(R.string.location_rational_text)

    override val btnText: String = context.getString(R.string.ok)

    override val image: Int = R.drawable.baseline_add_location_24

    override fun buttonClicked() {
        dismiss()
        okBtnClicked.invoke()
    }
}


