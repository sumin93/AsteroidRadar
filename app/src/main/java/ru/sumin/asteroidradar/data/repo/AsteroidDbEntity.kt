package ru.sumin.asteroidradar.data.repo

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.sumin.asteroidradar.domain.AsteroidEntity

@Entity(tableName = "asteroids")
data class AsteroidDbEntity(
    @PrimaryKey
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean
) {

    fun toDomainEntity(): AsteroidEntity {
        return AsteroidEntity(
                id,
                codename,
                closeApproachDate,
                absoluteMagnitude,
                estimatedDiameter,
                relativeVelocity,
                distanceFromEarth,
                isPotentiallyHazardous
        )
    }
}
