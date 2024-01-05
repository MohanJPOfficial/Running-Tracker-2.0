package com.mohanjp.runningtracker.feature.runtrack.domain.model

data class Run(
    val id: String,
    val imageByteArray: ByteArray,
    val timestamp: Long,
    val avgSpeedInKMH: Float,
    val distanceInMeters: Int,
    val runTimeInMillis: Long,
    val caloriesBurned: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Run

        if (id != other.id) return false
        if (!imageByteArray.contentEquals(other.imageByteArray)) return false
        if (timestamp != other.timestamp) return false
        if (avgSpeedInKMH != other.avgSpeedInKMH) return false
        if (distanceInMeters != other.distanceInMeters) return false
        if (runTimeInMillis != other.runTimeInMillis) return false

        return caloriesBurned == other.caloriesBurned
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + imageByteArray.contentHashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + avgSpeedInKMH.hashCode()
        result = 31 * result + distanceInMeters
        result = 31 * result + runTimeInMillis.hashCode()
        result = 31 * result + caloriesBurned

        return result
    }
}