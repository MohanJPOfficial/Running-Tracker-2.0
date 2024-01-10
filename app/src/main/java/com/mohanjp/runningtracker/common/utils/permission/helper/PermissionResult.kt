package com.mohanjp.runningtracker.common.utils.permission.helper

sealed interface PermissionResult {
    data object PermissionGranted : PermissionResult
    data class Error(val error: String) : PermissionResult
}