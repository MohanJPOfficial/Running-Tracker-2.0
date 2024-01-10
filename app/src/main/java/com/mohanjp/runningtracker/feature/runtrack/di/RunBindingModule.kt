package com.mohanjp.runningtracker.feature.runtrack.di

import com.mohanjp.runningtracker.feature.runtrack.data.repository.RunRepositoryImpl
import com.mohanjp.runningtracker.feature.runtrack.data.source.local.datasource.RunLocalDatasource
import com.mohanjp.runningtracker.feature.runtrack.data.source.local.datasource.RunLocalDatasourceImpl
import com.mohanjp.runningtracker.feature.runtrack.domain.repository.RunRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RunBindingModule {

    @Binds
    @Singleton
    fun bindRunRepository(
        runRepository: RunRepositoryImpl
    ): RunRepository

    @Binds
    @Singleton
    fun bindRunLocalDatasource(
        runLocalDatasource: RunLocalDatasourceImpl
    ): RunLocalDatasource
}