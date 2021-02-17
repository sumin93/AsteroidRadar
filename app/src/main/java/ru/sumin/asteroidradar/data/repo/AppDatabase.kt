package ru.sumin.asteroidradar.data.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidDbEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun asteroidDao(): AsteroidDao

    companion object {

        private const val DB_NAME = "asteroid.db"

        private val lock = Any()
        private var database: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            database?.let { return it }
            synchronized(lock) {
                database?.let { return it }

                val instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        DB_NAME
                ).build()

                database = instance
                return instance
            }
        }
    }
}
