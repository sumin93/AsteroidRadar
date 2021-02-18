package ru.sumin.asteroidradar.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import ru.sumin.asteroidradar.data.repo.AppRepositoryImpl
import ru.sumin.asteroidradar.domain.AppRepository
import ru.sumin.asteroidradar.domain.Period
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository = AppRepositoryImpl(application.applicationContext)

    private val periodOfAsteroidsData = MutableLiveData<Period>(Period.WEEK)

    val asteroids = Transformations.switchMap(periodOfAsteroidsData) {
        repository.getAsteroids(it)
    }

    private val _error = MutableLiveData<Unit>()
    val error: LiveData<Unit> = _error

    val pictureOfDay = repository.getPictureOfDay()

    fun getAsteroids() {
        viewModelScope.launch {
            try {
                repository.refreshData()
            } catch (e: Exception) {
                if (asteroids.value.isNullOrEmpty()) {
                    _error.value = Unit
                }
                Log.d(LOG_TAG, "Error in init block", e)
            }
        }
    }

    fun showWeekAsteroids() {
        periodOfAsteroidsData.value = Period.WEEK
    }

    fun showTodayAsteroids() {
        periodOfAsteroidsData.value = Period.TODAY
    }

    fun showSavedAsteroids() {
        periodOfAsteroidsData.value = Period.ALL
    }

    fun errorProcessed() {
        _error.value = null
    }

    private companion object {

        const val LOG_TAG = "MainViewModel"
    }
}
