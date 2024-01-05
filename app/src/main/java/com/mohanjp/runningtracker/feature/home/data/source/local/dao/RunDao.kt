package com.mohanjp.runningtracker.feature.home.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.mohanjp.runningtracker.feature.home.data.source.local.entity.RunEntity
import com.mohanjp.runningtracker.feature.home.data.source.local.entity.RunTable
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    @Upsert
    suspend fun insertRun(run: RunEntity)

    @Delete
    suspend fun deleteRun(run: RunEntity)

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.TIMESTAMP} DESC")
    fun getAllRunsSortedByDate(): Flow<List<RunEntity>>

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.RUN_TIME_IN_MILLIS} DESC")
    fun getAllRunsSortedByRunTimeInMillis(): Flow<List<RunEntity>>

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.CALORIES_BURNED} DESC")
    fun getAllRunsSortedByCaloriesBurned(): Flow<List<RunEntity>>

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.AVG_SPEED_IN_KMH} DESC")
    fun getAllRunsSortedByAvgSpeed(): Flow<List<RunEntity>>

    @Query("SELECT * FROM ${RunTable.TABLE_NAME} ORDER BY ${RunTable.Column.DISTANCE_IN_METERS} DESC")
    fun getAllRunsSortedByDistance(): Flow<List<RunEntity>>

    @Query("SELECT SUM(${RunTable.Column.RUN_TIME_IN_MILLIS}) FROM ${RunTable.TABLE_NAME}")
    fun getTotalRunTimeInMillis(): Flow<Long>

    @Query("SELECT SUM(${RunTable.Column.CALORIES_BURNED}) FROM ${RunTable.TABLE_NAME}")
    fun getTotalCaloriesBurned(): Flow<Int>

    @Query("SELECT SUM(${RunTable.Column.DISTANCE_IN_METERS}) FROM ${RunTable.TABLE_NAME}")
    fun getTotalDistance(): Flow<Int>

    @Query("SELECT SUM(${RunTable.Column.AVG_SPEED_IN_KMH}) FROM ${RunTable.TABLE_NAME}")
    fun getTotalAvgSpeed(): Flow<Float>
}