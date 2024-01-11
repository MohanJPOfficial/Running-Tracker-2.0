package com.mohanjp.runningtracker.common.utils.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.mohanjp.runningtracker.common.utils.permission.helper.AppPermissionEnum

fun Context.openSettings() {

    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Context.checkHasPermission(permission: AppPermissionEnum): Boolean =
    ActivityCompat.checkSelfPermission(this, permission.value) == PackageManager.PERMISSION_GRANTED

fun Context.checkHasLocationPermission() =
    checkHasPermission(AppPermissionEnum.COARSE_LOCATION) && checkHasPermission(AppPermissionEnum.FINE_LOCATION)

fun Context.getLocationManager() =
    getSystemService(Context.LOCATION_SERVICE) as LocationManager

fun Context.checkHasGPSEnabled() =
    getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)

fun Context.checkHasNetworkEnabled() =
    getLocationManager().isProviderEnabled(LocationManager.NETWORK_PROVIDER)