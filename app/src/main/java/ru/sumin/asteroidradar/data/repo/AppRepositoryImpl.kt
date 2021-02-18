package ru.sumin.asteroidradar.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.sumin.asteroidradar.data.api.ApiFactory
import ru.sumin.asteroidradar.data.api.parseAsteroidsJsonResult
import ru.sumin.asteroidradar.data.dto.PictureOfDayDto
import ru.sumin.asteroidradar.domain.AppRepository
import ru.sumin.asteroidradar.domain.AsteroidEntity
import ru.sumin.asteroidradar.domain.Period
import ru.sumin.asteroidradar.domain.PictureOfDayEntity

class AppRepositoryImpl(context: Context) : AppRepository {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val moshiAdapter = moshi.adapter(PictureOfDayDto::class.java)

    private val apiService = ApiFactory.apiService
    private val db = AppDatabase.getInstance(context)
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _pictureOfDay = MutableLiveData<PictureOfDayEntity>()

    override fun getAsteroids(period: Period): LiveData<List<AsteroidEntity>> {
        val result = when (period) {
            Period.ALL -> db.asteroidDao().getAllAsteroids()
            Period.WEEK -> db.asteroidDao().getWeekAsteroids()
            Period.TODAY -> db.asteroidDao().getTodayAsteroids()
        }
        return Transformations.map(result) {
            it.map { it.toDomainEntity() }
        }
    }

    override fun getPictureOfDay() = _pictureOfDay

    override suspend fun refreshData() {
        loadPictureOfDayFromCache()
        loadPictureOfDayFromNetwork()
        loadAsteroidsFromNetwork()
    }

    override suspend fun clearOutdatedData() {
        db.asteroidDao().clearOutdatedData()
    }

    private suspend fun loadPictureOfDayFromCache() {
        withContext(Dispatchers.IO) {
            val savedJson = prefs.getString(PICTURE_OF_DAY_KEY, null)
            savedJson?.let {
                val pictureOfDayEntity = moshiAdapter.fromJson(it)?.toEntity()
                _pictureOfDay.postValue(pictureOfDayEntity)
            }
        }
    }

    private suspend fun loadAsteroidsFromNetwork() {
        withContext(Dispatchers.IO) {
            val asteroidResponse = apiService.getAsteroids()
            val asteroids = parseAsteroidsJsonResult(asteroidResponse)
            db.asteroidDao().insertAsteroids(asteroids.map { it.toDbEntity() })
        }
    }

    private suspend fun loadPictureOfDayFromNetwork() {
        withContext(Dispatchers.IO) {
            val pictureOfDayDto = apiService.getPictureOfDay()
            _pictureOfDay.postValue(pictureOfDayDto.toEntity())
            val pictureOfDayString = moshiAdapter.toJson(pictureOfDayDto)
            prefs.edit().putString(PICTURE_OF_DAY_KEY, pictureOfDayString).apply()
        }
    }

    private companion object {

        const val PREFS_NAME = "app.prefs"
        const val PICTURE_OF_DAY_KEY = "picture_of_day"
    }
}
