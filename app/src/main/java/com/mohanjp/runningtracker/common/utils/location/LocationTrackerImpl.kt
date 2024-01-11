package com.mohanjp.runningtracker.common.utils.location

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mohanjp.runningtracker.common.utils.presentation.checkHasGPSEnabled
import com.mohanjp.runningtracker.common.utils.presentation.checkHasLocationPermission
import com.mohanjp.runningtracker.common.utils.presentation.checkHasNetworkEnabled
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

abstract class LocationTrackerImpl(
    private val context: Context
) : LocationTracker {

    abstract val locationRequestBuilder: LocationRequest.Builder

    private lateinit var locationCallback: LocationCallback

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    override fun startLocationTracking() {
        if(!context.checkHasLocationPermission()) {
            throw LocationException("missing location permissions..")
        }

        if(!context.checkHasGPSEnabled()) {
            throw LocationException("GPS isn't enabled..")
        }

        if(!context.checkHasNetworkEnabled()) {
            throw LocationException("network isn't enabled..")
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequestBuilder.build(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun stopLocationTracking() {
        fusedLocationProviderClient.flushLocations()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override val locationUpdatesFlow: Flow<Location> = callbackFlow {

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.lastOrNull()?.let { location ->
                    launch { send(location) }
                }
            }
        }

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }
}