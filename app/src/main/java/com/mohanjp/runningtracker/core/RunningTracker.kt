package com.mohanjp.runningtracker.core

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import com.mohanjp.runningtracker.common.utils.notifcation.channel.NotificationChannels
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RunningTracker: Application() {

    override fun onCreate() {
        super.onCreate()

        plantTimberDebugTree()
        setupNotificationChannels()
    }

    private fun plantTimberDebugTree() {
        Timber.plant(Timber.DebugTree())
    }

    private fun setupNotificationChannels() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(
            NotificationChannels.entries.map {
                it.getNotificationChannel(applicationContext)
            }
        )
    }
}