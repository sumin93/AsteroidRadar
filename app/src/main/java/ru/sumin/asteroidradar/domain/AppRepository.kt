package ru.sumin.asteroidradar.domain

import androidx.lifecycle.LiveData

interface AppRepository {

    fun getAsteroids(period: Period): LiveData<List<AsteroidEntity>>

    fun getPictureOfDay(): LiveData<PictureOfDayEntity>

    suspend fun refreshData()

    suspend fun clearOutdatedData()
}
