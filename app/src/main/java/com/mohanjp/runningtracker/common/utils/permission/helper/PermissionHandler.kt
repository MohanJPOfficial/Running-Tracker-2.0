package com.mohanjp.runningtracker.common.utils.permission.helper

import androidx.lifecycle.LifecycleObserver

interface PermissionHandler : LifecycleObserver {

    fun onViewCreated()

    fun requestPermission()
}