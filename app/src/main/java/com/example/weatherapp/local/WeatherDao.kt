package com.example.weatherapp.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.remote.response.weatherResponse.WeatherResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface to communicate with room database
 * Used for CRUD operations in room db
 */
@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherResponse: List<LocalWeatherData?>)

    @Query("SELECT * FROM weather_table")
    fun getWeatherFromLocal(): Flow<List<LocalWeatherData>>

}