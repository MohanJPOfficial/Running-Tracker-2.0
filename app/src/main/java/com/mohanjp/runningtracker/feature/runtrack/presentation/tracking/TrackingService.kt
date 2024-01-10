package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.mohanjp.runningtracker.common.utils.notifcation.service.NotificationService
import com.mohanjp.runningtracker.common.utils.notifcation.service.RunningTrackerNotificationService
import timber.log.Timber


class TrackingService : LifecycleService() {

    private val trackerNotification: NotificationService by lazy {
        RunningTrackerNotificationService(this)
    }

    var isFirstRun = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                TrackingServiceState.ACTION_START_OR_RESUME_SERVICE.name -> {
                    Timber.d("service was started/resumed")

                    if(isFirstRun) {
                        isFirstRun = false
                        startForegroundService()
                    }
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

    private fun startForegroundService() {
        startForeground(
            trackerNotification.notificationId,
            trackerNotification.notificationBuilder.build()
        )
    }
}

enum class TrackingServiceState {
    ACTION_START_OR_RESUME_SERVICE,
    ACTION_PAUSE_SERVICE,
    ACTION_STOP_SERVICE
}