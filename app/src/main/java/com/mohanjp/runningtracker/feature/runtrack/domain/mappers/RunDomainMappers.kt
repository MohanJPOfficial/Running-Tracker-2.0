package com.mohanjp.runningtracker.feature.runtrack.domain.mappers

import com.mohanjp.runningtracker.feature.runtrack.data.source.local.entity.RunEntity
import com.mohanjp.runningtracker.feature.runtrack.domain.model.Run

fun Run.toEntity() = RunEntity(
    id = id,
    imageByteArray = imageByteArray,
    timestamp = timestamp,
    avgSpeedInKMH = avgSpeedInKMH,
    distanceInMeters = distanceInMeters,
    runTimeInMillis = runTimeInMillis,
    caloriesBurned = caloriesBurned
)