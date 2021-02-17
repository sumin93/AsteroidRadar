package ru.sumin.asteroidradar.data.dto

import com.squareup.moshi.Json
import ru.sumin.asteroidradar.domain.PictureOfDayEntity

data class PictureOfDayDto(
        @Json(name = "media_type") val mediaType: String,
        val title: String,
        val url: String
) {

    fun toEntity() = PictureOfDayEntity(mediaType, title, url)
}
