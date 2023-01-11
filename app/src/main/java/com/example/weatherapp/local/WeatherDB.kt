package com.example.weatherapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.remote.response.weatherResponse.WeatherTypeConverter

/**
 * Creating singleton room database object
 * As we want no 2 CRUD operations are run simultaneously in db so we use Synchronized()
 * Synchronized() is used to ensure that a block of code is only executed by one thread at a time.
 */

@Database(entities = [LocalWeatherData::class], version = 1, exportSchema = false)
@TypeConverters(WeatherTypeConverter::class)
abstract class WeatherDB:RoomDatabase() {
    abstract fun getDao() : WeatherDao

    companion object {
        private const val DB_NAME = "weather_database"

        @Volatile
        private var INSTANCE: WeatherDB? = null

        /**
         * Checking if the instance exists or not
         * If yes, then return it
         * else generate one
         */

        fun getInstance(context: Context): WeatherDB {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                // Creating the instance of the Database
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDB::class.java,
                    DB_NAME
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}