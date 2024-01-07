package com.mohanjp.runningtracker.common.utils

import android.os.Build

/**
 * Android 10
 */
fun isAndroid29OrAbove() =
    Build.VERSION_CODES.Q >= Build.VERSION.SDK_INT

/**
 * Android 13
 */
fun isAndroid33OrAbove() =
    Build.VERSION_CODES.TIRAMISU >= Build.VERSION.SDK_INT