package com.mohanjp.runningtracker.feature.home.data.source.local.datasource

import com.mohanjp.runningtracker.feature.home.data.source.local.entity.RunEntity
import kotlinx.coroutines.flow.Flow

interface RunLocalDatasource {

    suspend fun upsertRun(run: RunEntity)

    suspend fun deleteRun(run: RunEntity)

    fun getAllRunsSortedByDate(): Flow<List<RunEntity>>

    fun getAllRunsSortedByRunTimeInMillis(): Flow<List<RunEntity>>

    fun getAllRunsSortedByCaloriesBurned(): Flow<List<RunEntity>>

    fun getAllRunsSortedByAvgSpeed(): Flow<List<RunEntity>>

    fun getAllRunsSortedByDistance(): Flow<List<RunEntity>>

    fun getTotalRunTimeInMillis(): Flow<Long>

    fun getTotalCaloriesBurned(): Flow<Int>

    fun getTotalDistance(): Flow<Int>

    fun getTotalAvgSpeed(): Flow<Float>
}