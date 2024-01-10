package com.mohanjp.runningtracker.common.utils.notifcation.service

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

abstract class NotificationService(
    context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    abstract val notificationId: Int

    abstract val notificationBuilder: NotificationCompat.Builder

    fun postNotification() {
        notificationManager.notify(
            notificationId,
            notificationBuilder.build()
        )
    }

    fun postNotification(contentText: String) {
        notificationManager.notify(
            notificationId,
            notificationBuilder
                .setContentText(contentText)
                .build()
        )
    }
}