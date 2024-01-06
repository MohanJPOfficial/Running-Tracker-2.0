package com.mohanjp.runningtracker.common.utils.permission.helper

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

enum class AppPermissionEnum(
    val value: String
) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    NOTIFICATION(Manifest.permission.POST_NOTIFICATIONS),

    COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
    FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),

    @RequiresApi(Build.VERSION_CODES.Q)
    BACKGROUND_LOCATION(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
}