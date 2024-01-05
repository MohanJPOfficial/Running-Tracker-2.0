package com.mohanjp.runningtracker.feature.home.data.source.local.datasource

import com.mohanjp.runningtracker.core.data.source.local.AppDB
import com.mohanjp.runningtracker.feature.home.data.source.local.entity.RunEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RunLocalDatasourceImpl @Inject constructor(
    private val appDB: AppDB
): RunLocalDatasource {

    override suspend fun upsertRun(run: RunEntity) {
        appDB.runDao.upsertRun(run)
    }

    override suspend fun deleteRun(run: RunEntity) {
        appDB.runDao.deleteRun(run)
    }

    override fun getAllRunsSortedByDate(): Flow<List<RunEntity>> {
        return appDB.runDao.getAllRunsSortedByDate()
    }

    override fun getAllRunsSortedByRunTimeInMillis(): Flow<List<RunEntity>> {
        return appDB.runDao.getAllRunsSortedByRunTimeInMillis()
    }

    override fun getAllRunsSortedByCaloriesBurned(): Flow<List<RunEntity>> {
        return appDB.runDao.getAllRunsSortedByCaloriesBurned()
    }

    override fun getAllRunsSortedByAvgSpeed(): Flow<List<RunEntity>> {
        return appDB.runDao.getAllRunsSortedByAvgSpeed()
    }

    override fun getAllRunsSortedByDistance(): Flow<List<RunEntity>> {
        return appDB.runDao.getAllRunsSortedByDistance()
    }

    override fun getTotalRunTimeInMillis(): Flow<Long> {
        return appDB.runDao.getTotalRunTimeInMillis()
    }

    override fun getTotalCaloriesBurned(): Flow<Int> {
        return appDB.runDao.getTotalCaloriesBurned()
    }

    override fun getTotalDistance(): Flow<Int> {
        return appDB.runDao.getTotalDistance()
    }

    override fun getTotalAvgSpeed(): Flow<Float> {
        return appDB.runDao.getTotalAvgSpeed()
    }
}