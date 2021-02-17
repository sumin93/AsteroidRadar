package ru.sumin.asteroidradar.data.repo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.sumin.asteroidradar.data.api.getFormattedDate

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroids WHERE closeApproachDate >= :currentDate ORDER BY closeApproachDate")
    fun getWeekAsteroids(
            currentDate: String = getFormattedDate()
    ): LiveData<List<AsteroidDbEntity>>

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate")
    fun getAllAsteroids(): LiveData<List<AsteroidDbEntity>>

    @Query("SELECT * FROM asteroids WHERE closeApproachDate == :currentDate ORDER BY closeApproachDate")
    fun getTodayAsteroids(
            currentDate: String = getFormattedDate()
    ): LiveData<List<AsteroidDbEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAsteroids(asteroids: List<AsteroidDbEntity>)

    @Query("DELETE FROM asteroids WHERE closeApproachDate < :currentDate")
    fun clearOutdatedData(
            currentDate: String = getFormattedDate()
    )
}
