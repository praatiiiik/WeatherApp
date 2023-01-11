package com.example.weatherapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.remote.response.weatherResponse.WeatherTypeConverter

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