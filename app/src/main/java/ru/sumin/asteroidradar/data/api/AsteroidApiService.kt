package ru.sumin.asteroidradar.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.sumin.asteroidradar.BuildConfig
import ru.sumin.asteroidradar.data.dto.PictureOfDayDto

interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String = getFormattedDate(0),
        @Query("end_date") endDate: String = getFormattedDate(7),
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): String

    @GET("planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): PictureOfDayDto
}