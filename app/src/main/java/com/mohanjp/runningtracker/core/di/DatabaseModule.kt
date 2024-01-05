package com.mohanjp.runningtracker.core.di

import android.content.Context
import androidx.room.Room
import com.mohanjp.runningtracker.core.data.source.local.AppDB
import com.mohanjp.runningtracker.core.data.source.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDB(
        @ApplicationContext
        context: Context
    ): AppDB {
        return Room.databaseBuilder(
            context = context,
            klass   = AppDB::class.java,
            name    = AppDatabase.NAME
        ).fallbackToDestructiveMigration()
            .build()
    }
}