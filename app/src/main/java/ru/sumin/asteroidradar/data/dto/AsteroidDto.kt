package ru.sumin.asteroidradar.data.dto

import ru.sumin.asteroidradar.data.repo.AsteroidDbEntity

data class AsteroidDto(
        val id: Long,
        val codename: String,
        val closeApproachDate: String,
        val absoluteMagnitude: Double,
        val estimatedDiameter: Double,
        val relativeVelocity: Double,
        val distanceFromEarth: Double,
        val isPotentiallyHazardous: Boolean
) {

    fun toDbEntity(): AsteroidDbEntity {
        return AsteroidDbEntity(
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
