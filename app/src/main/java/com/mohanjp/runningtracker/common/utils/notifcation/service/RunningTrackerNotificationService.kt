package com.mohanjp.runningtracker.common.utils.notifcation.service

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.mohanjp.runningtracker.CoreActivity
import com.mohanjp.runningtracker.R
import com.mohanjp.runningtracker.common.utils.notifcation.channel.NotificationChannels

class RunningTrackerNotificationService(
    private val context: Context
) : NotificationService(context) {

    override val notificationId: Int =
        NotificationChannels.RUNNING_TRACKER_NOTIFICATION_CHANNEL.notificationId

    override val notificationBuilder: NotificationCompat.Builder =
        NotificationCompat.Builder(
            context,
            NotificationChannels.RUNNING_TRACKER_NOTIFICATION_CHANNEL.getChannelId(context)
        ).setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.baseline_assist_walker_24)
            .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
            .setContentTitle(context.getString(R.string.running_tracker_notification_content_title))
            .setContentIntent(getPendingIntent())

    private fun getPendingIntent() = NavDeepLinkBuilder(context)
        .setComponentName(CoreActivity::class.java)
        .setGraph(R.navigation.core_nav_graph)
        .setDestination(R.id.trackingFragment)
        .createPendingIntent()
}