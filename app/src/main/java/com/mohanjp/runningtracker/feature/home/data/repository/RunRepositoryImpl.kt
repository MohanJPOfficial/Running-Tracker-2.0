package com.mohanjp.runningtracker.feature.home.data.repository

import com.mohanjp.runningtracker.feature.home.data.mappers.toRun
import com.mohanjp.runningtracker.feature.home.data.source.local.datasource.RunLocalDatasource
import com.mohanjp.runningtracker.feature.home.data.source.local.entity.RunEntity
import com.mohanjp.runningtracker.feature.home.domain.mappers.toEntity
import com.mohanjp.runningtracker.feature.home.domain.model.Run
import com.mohanjp.runningtracker.feature.home.domain.repository.RunRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RunRepositoryImpl @Inject constructor(
    private val localDatasource: RunLocalDatasource
): RunRepository {

    override suspend fun upsertRun(run: Run) {
        localDatasource.upsertRun(run.toEntity())
    }

    override suspend fun deleteRun(run: Run) {
        localDatasource.deleteRun(run.toEntity())
    }

    override fun getAllRunsSortedByDate(): Flow<List<Run>> {
        return localDatasource.getAllRunsSortedByDate().map { list ->
            list.map(RunEntity::toRun)
        }
    }

    override fun getAllRunsSortedByRunTimeInMillis(): Flow<List<Run>> {
        return localDatasource.getAllRunsSortedByRunTimeInMillis().map { list ->
            list.map(RunEntity::toRun)
        }
    }

    override fun getAllRunsSortedByCaloriesBurned(): Flow<List<Run>> {
        return localDatasource.getAllRunsSortedByCaloriesBurned().map { list ->
            list.map(RunEntity::toRun)
        }
    }

    override fun getAllRunsSortedByAvgSpeed(): Flow<List<Run>> {
        return localDatasource.getAllRunsSortedByAvgSpeed().map { list ->
            list.map(RunEntity::toRun)
        }
    }

    override fun getAllRunsSortedByDistance(): Flow<List<Run>> {
        return localDatasource.getAllRunsSortedByDistance().map { list ->
            list.map(RunEntity::toRun)
        }
    }

    override fun getTotalRunTimeInMillis(): Flow<Long> {
        return localDatasource.getTotalRunTimeInMillis()
    }

    override fun getTotalCaloriesBurned(): Flow<Int> {
        return localDatasource.getTotalCaloriesBurned()
    }

    override fun getTotalDistance(): Flow<Int> {
        return localDatasource.getTotalDistance()
    }

    override fun getTotalAvgSpeed(): Flow<Float> {
        return localDatasource.getTotalAvgSpeed()
    }
}