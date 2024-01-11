package com.mohanjp.runningtracker.feature.runtrack.presentation.tracking

import android.content.Intent
import android.location.Location
import androidx.lifecycle.LifecycleService
import com.google.android.gms.maps.model.LatLng
import com.mohanjp.runningtracker.common.utils.location.LocationTracker
import com.mohanjp.runningtracker.common.utils.location.RunningLocationTracker
import com.mohanjp.runningtracker.common.utils.notifcation.service.NotificationService
import com.mohanjp.runningtracker.common.utils.notifcation.service.RunningTrackerNotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService : LifecycleService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        val isTracking = MutableStateFlow(false)
        val pathPoints = MutableStateFlow<Polylines>(mutableListOf(mutableListOf()))

        val dummy = MutableStateFlow(listOf<LatLng>())
    }

    private val trackerNotification: NotificationService by lazy {
        RunningTrackerNotificationService(this)
    }

    private val locationTracker: LocationTracker by lazy {
        RunningLocationTracker(this)
    }

    var isFirstRun = true

    override fun onCreate() {
        super.onCreate()

        locationTracker.locationUpdatesFlow
            .onEach { location ->

                addPathPoint(location)

            }.launchIn(serviceScope)

        serviceScope.launch {
            pathPoints.collect {
                Timber.d("polylines >> $it")
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (intent.action) {
                TrackingServiceState.ACTION_START_OR_RESUME_SERVICE.name -> {
                    Timber.d("service was started/resumed")

                    if(isFirstRun) {
                        isFirstRun = false
                        startForegroundService()
                    } else {
                        startForegroundService()
                    }
                }
                TrackingServiceState.ACTION_PAUSE_SERVICE.name -> {
                    pauseLocationTracking()
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

        addEmptyPolyline()
        isTracking.update { true }

        locationTracker.startLocationTracking()

        startForeground(
            trackerNotification.notificationId,
            trackerNotification.notificationBuilder.build()
        )
    }

    private fun pauseLocationTracking() {
        isTracking.update { false }
        locationTracker.stopLocationTracking()
    }

    private fun addEmptyPolyline() {

        pathPoints.update {
            it.also { list ->
                list.add(mutableListOf())
            }
        }
    }

    private fun addPathPoint(location: Location) {

        val latLng = LatLng(location.latitude, location.longitude)

        dummy.update {
            it.toMutableList().also { list ->
                list.add(latLng)
            }.toList()
        }

        Timber.d("Location update => lat = ${location.latitude}, lng = ${location.longitude}")

        Timber.d("current list = ${pathPoints.value}")

        pathPoints.update {
            pathPoints.value.apply {
                last().also {
                    it.add(latLng)
                }
            }
        }
    }
}

enum class TrackingServiceState {
    ACTION_START_OR_RESUME_SERVICE,
    ACTION_PAUSE_SERVICE,
    ACTION_STOP_SERVICE
}