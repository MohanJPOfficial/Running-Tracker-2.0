package com.mohanjp.runningtracking.feature.home.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RunEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = RunTable.Column.ID)
    val id: String,

    @ColumnInfo(name = RunTable.Column.IMAGE_BYTE_ARRAY)
    val imageByteArray: ByteArray,

    @ColumnInfo(name = RunTable.Column.TIMESTAMP)
    val timestamp: Long,

    @ColumnInfo(name = RunTable.Column.AVG_SPEED_IN_KMH)
    val avgSpeedInKMH: Float,

    @ColumnInfo(name = RunTable.Column.DISTANCE_IN_METERS)
    val distanceInMeters: Int,

    @ColumnInfo(name = RunTable.Column.TIME_IN_MILLIS)
    val timeInMillis: Long,

    @ColumnInfo(name = RunTable.Column.CALORIES_BURNED)
    val caloriesBurned: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RunEntity

        if (id != other.id) return false
        if (!imageByteArray.contentEquals(other.imageByteArray)) return false
        if (timestamp != other.timestamp) return false
        if (avgSpeedInKMH != other.avgSpeedInKMH) return false
        if (distanceInMeters != other.distanceInMeters) return false
        if (timeInMillis != other.timeInMillis) return false

        return caloriesBurned == other.caloriesBurned
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + imageByteArray.contentHashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + avgSpeedInKMH.hashCode()
        result = 31 * result + distanceInMeters
        result = 31 * result + timeInMillis.hashCode()
        result = 31 * result + caloriesBurned

        return result
    }
}

object RunTable {
    const val TABLE_NAME = "run_table"

    object Column {
        const val ID                 = "id"
        const val IMAGE_BYTE_ARRAY   = "image"
        const val TIMESTAMP          = "timestamp"
        const val AVG_SPEED_IN_KMH   = "avg_speed_in_kmh"
        const val DISTANCE_IN_METERS = "distance_in_meters"
        const val TIME_IN_MILLIS     = "time_in_millis"
        const val CALORIES_BURNED    = "calories_burned"
    }
}