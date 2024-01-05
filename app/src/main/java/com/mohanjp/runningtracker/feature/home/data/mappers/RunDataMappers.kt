package com.mohanjp.runningtracker.feature.home.data.mappers

import com.mohanjp.runningtracker.feature.home.data.source.local.entity.RunEntity
import com.mohanjp.runningtracker.feature.home.domain.model.Run

fun RunEntity.toRun() = Run(
    id = id,
    imageByteArray = imageByteArray,
    timestamp = timestamp,
    avgSpeedInKMH = avgSpeedInKMH,
    distanceInMeters = distanceInMeters,
    runTimeInMillis = runTimeInMillis,
    caloriesBurned = caloriesBurned
)