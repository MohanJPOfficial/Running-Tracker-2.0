package com.mohanjp.runningtracker.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohanjp.runningtracker.feature.home.data.source.local.dao.RunDao
import com.mohanjp.runningtracker.feature.home.data.source.local.entity.RunEntity

@Database(
    entities = [RunEntity::class],
    version = 1
)
abstract class AppDB : RoomDatabase() {

    abstract val runDao: RunDao
}

object AppDatabase {
    const val NAME = "running_tracker.db"
}