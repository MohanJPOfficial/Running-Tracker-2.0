package com.mohanjp.runningtracker.feature.runtrack.domain.repository

import com.mohanjp.runningtracker.feature.runtrack.domain.model.Run
import kotlinx.coroutines.flow.Flow

interface RunRepository {

    suspend fun upsertRun(run: Run)

    suspend fun deleteRun(run: Run)

    fun getAllRunsSortedByDate(): Flow<List<Run>>

    fun getAllRunsSortedByRunTimeInMillis(): Flow<List<Run>>

    fun getAllRunsSortedByCaloriesBurned(): Flow<List<Run>>

    fun getAllRunsSortedByAvgSpeed(): Flow<List<Run>>

    fun getAllRunsSortedByDistance(): Flow<List<Run>>

    fun getTotalRunTimeInMillis(): Flow<Long>

    fun getTotalCaloriesBurned(): Flow<Int>

    fun getTotalDistance(): Flow<Int>

    fun getTotalAvgSpeed(): Flow<Float>
}