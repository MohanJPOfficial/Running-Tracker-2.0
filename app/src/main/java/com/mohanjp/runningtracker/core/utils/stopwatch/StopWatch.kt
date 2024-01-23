package com.mohanjp.runningtracker.core.utils.stopwatch

import kotlinx.coroutines.flow.StateFlow

interface StopWatch {

    val formattedTime: StateFlow<StopwatchFormat>

    fun start()

    fun pause()

    fun reset()
}