package ru.sumin.asteroidradar.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
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

    val pictureOfDay = repository.getPictureOfDay()

    init {
        viewModelScope.launch {
            try {
                repository.refreshData()
            } catch (e: Exception) {
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

    private companion object {

        const val LOG_TAG = "MainViewModel"
    }
}
