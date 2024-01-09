package com.mohanjp.runningtracker.common.utils

import android.os.Build

/**
 * Android 10
 */
fun isAndroid29OrAbove() =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

/**
 * Android 13
 */
fun isAndroid33OrAbove() =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU