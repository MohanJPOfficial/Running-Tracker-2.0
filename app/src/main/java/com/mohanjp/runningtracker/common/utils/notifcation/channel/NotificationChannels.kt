package com.mohanjp.runningtracker.common.utils.notifcation.channel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.StringRes
import com.mohanjp.runningtracker.R

enum class NotificationChannels(
    @StringRes val channelId: Int,
    @StringRes val channelName: Int,
    @StringRes val channelDescription: Int,
    val channelImportance: Int,
    val notificationId: Int
) {
    RUNNING_TRACKER_NOTIFICATION_CHANNEL(
        channelId = R.string.running_tracker_notification_channel_id,
        channelName = R.string.title_running_tracker_notification,
        channelDescription = R.string.desc_running_tracker_notification,
        channelImportance = NotificationManager.IMPORTANCE_DEFAULT,
        notificationId = 100
    );

    fun getNotificationChannel(context: Context): NotificationChannel {
        return NotificationChannel(
            /* id = */ context.getString(channelId),
            /* name = */ context.getString(channelName),
            /* importance = */ channelImportance
        ).also {
            it.description = context.getString(channelDescription)
        }
    }

    fun getChannelId(context: Context) =
        context.getString(channelId)
}