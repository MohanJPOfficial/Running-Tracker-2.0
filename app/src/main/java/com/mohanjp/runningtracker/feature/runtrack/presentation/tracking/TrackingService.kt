package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import com.google.android.gms.maps.model.LatLng
import com.mohanjp.runningtracker.common.utils.location.LocationTracker
import com.mohanjp.runningtracker.common.utils.location.RunningLocationTracker
import com.mohanjp.runningtracker.common.utils.notifcation.service.NotificationService
import com.mohanjp.runningtracker.common.utils.notifcation.service.RunningTrackerNotificationService
import com.mohanjp.runningtracker.core.utils.stopwatch.RunningTrackerStopWatch
import com.mohanjp.runningtracker.core.utils.stopwatch.StopWatch
import com.mohanjp.runningtracker.core.utils.stopwatch.StopwatchFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val pathPoints = mutableListOf<Polyline>()

    private val trackingServiceBinder = TrackingServiceBinder()

    val isTracking = MutableStateFlow(false)
    val pathPointsFlow = MutableSharedFlow<Polylines>()

    val stateFlow = MutableStateFlow(mutableListOf<Polyline>())

    val stopWatchTimer = MutableStateFlow(StopwatchFormat())

    private val trackerNotification: NotificationService by lazy {
        RunningTrackerNotificationService(this)
    }

    private val locationTracker: LocationTracker by lazy {
        RunningLocationTracker(this)
    }

    private val stopwatch: StopWatch by lazy {
        RunningTrackerStopWatch(scope = serviceScope)
    }

    private var isFirstRun = true

    override fun onCreate() {
        super.onCreate()

        locationTracker.locationUpdatesFlow
            .onEach { location ->
                addPathPoint(location)
            }.launchIn(serviceScope)
    }

    override fun onBind(intent: Intent): IBinder {
        return trackingServiceBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                TrackingServiceActionEnum.ACTION_START_OR_RESUME_SERVICE.name -> {
                    Timber.d("service was started/resumed")

                    if(isFirstRun) {
                        isFirstRun = false
                        startForegroundService()
                    } else {
                        startTimer()
                    }
                }
                TrackingServiceActionEnum.ACTION_PAUSE_SERVICE.name -> {
                    pauseLocationTracking()
                    Timber.d("service was paused")
                }
                TrackingServiceActionEnum.ACTION_STOP_SERVICE.name -> {
                    Timber.d("service was stopped")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer() {

        serviceScope.launch {
            addEmptyPolyline()
        }

        isTracking.update { true }

        stopwatch.start()

        locationTracker.startLocationTracking()

        stopwatch.formattedTime.onEach { stopwatchFormat ->
            stopWatchTimer.update { stopwatchFormat }
            trackerNotification.postNotification(contentText = stopwatchFormat.withoutMillis)
        }.launchIn(serviceScope)
    }

    private fun startForegroundService() {

        startTimer()

        startForeground(
            trackerNotification.notificationId,
            trackerNotification.notificationBuilder.build()
        )
    }

    private fun pauseLocationTracking() {
        isTracking.update { false }
        locationTracker.stopLocationTracking()
        stopwatch.pause()
    }

    private suspend fun addEmptyPolyline() {

        pathPoints.add(mutableListOf())

        pathPointsFlow.emit(pathPoints)
        stateFlow.value = pathPoints
    }

    private suspend fun addPathPoint(location: Location) {

        val latLng = LatLng(location.latitude, location.longitude)

        Timber.d("Location update => lat = ${location.latitude}, lng = ${location.longitude}")

        Timber.d("current list = $pathPoints")

        pathPoints.last().add(latLng)

        pathPointsFlow.emit(pathPoints)
        stateFlow.value = pathPoints
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("service was destroyed")
        serviceScope.cancel()
    }

    inner class TrackingServiceBinder : Binder() {
        fun getBoundService(): TrackingService = this@TrackingService
    }
}