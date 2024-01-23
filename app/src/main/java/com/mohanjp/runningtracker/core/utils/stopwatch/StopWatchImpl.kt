package com.mohanjp.runningtracker.core.utils.stopwatch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class StopWatchImpl(
    private val initialValue: StopwatchFormat,
    scope: CoroutineScope
) : StopWatch {

    private val _formattedTime = MutableStateFlow(initialValue)
    override val formattedTime = _formattedTime.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialValue
    )

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private var isTimerActive = false

    private var timeInMillis = 0L
    private var lastTimestamp = 0L

    override fun start() {
        if(isTimerActive)
            return

        coroutineScope.launch {
            lastTimestamp = System.currentTimeMillis()
            isTimerActive = true

            while(isTimerActive) {
                delay(10L)
                timeInMillis += System.currentTimeMillis() - lastTimestamp
                lastTimestamp = System.currentTimeMillis()

                _formattedTime.update {
                    formatTime(timeInMillis)
                }
            }
        }
    }

    override fun pause() {
        isTimerActive = false
    }

    override fun reset() {
        coroutineScope.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Main)
        timeInMillis = 0L
        lastTimestamp = 0L

        _formattedTime.update { initialValue }

        isTimerActive = false
    }

    abstract fun formatTime(timeInMillis: Long): StopwatchFormat
}