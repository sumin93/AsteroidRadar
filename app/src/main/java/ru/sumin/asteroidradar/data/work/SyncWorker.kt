package ru.sumin.asteroidradar.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ru.sumin.asteroidradar.data.repo.AppRepositoryImpl

class SyncWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params) {

    private val repository = AppRepositoryImpl(appContext)

    override suspend fun doWork(): Result {
        return try {
            repository.refreshData()
            repository.clearOutdatedData()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "SyncWorker"
    }
}
