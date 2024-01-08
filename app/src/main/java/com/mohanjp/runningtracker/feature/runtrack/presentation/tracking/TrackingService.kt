package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

import android.content.Intent
import androidx.lifecycle.LifecycleService
import timber.log.Timber


class TrackingService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                TrackingServiceState.ACTION_START_OR_RESUME_SERVICE.name -> {
                    Timber.d("service was started/resumed")
                }
                TrackingServiceState.ACTION_PAUSE_SERVICE.name -> {
                    Timber.d("service was paused")
                }
                TrackingServiceState.ACTION_STOP_SERVICE.name -> {
                    Timber.d("service was stopped")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}

enum class TrackingServiceState {
    ACTION_START_OR_RESUME_SERVICE,
    ACTION_PAUSE_SERVICE,
    ACTION_STOP_SERVICE
}