package com.mohanjp.runningtracker.core.utils.stopwatch

import kotlinx.coroutines.CoroutineScope

class RunningTrackerStopWatch(
    initialValue: StopwatchFormat = StopwatchFormat(),
    scope: CoroutineScope
): StopWatchImpl(
    initialValue,
    scope
) {

    override fun formatTime(timeInMillis: Long): StopwatchFormat {

        val hours   = (timeInMillis / (1000 * 60 * 60)) % 24
        val minutes = (timeInMillis / (1000 * 60)) % 60
        val seconds = (timeInMillis / 1000) % 60
        val milliseconds = (timeInMillis % 1000) / 10

        val withMillis = String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, milliseconds)
        val withoutMillis = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        return StopwatchFormat(withMillis = withMillis, withoutMillis = withoutMillis)
    }
}