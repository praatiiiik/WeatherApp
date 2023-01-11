package com.example.weatherapp.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.remote.response.weatherResponse.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherResponse: List<LocalWeatherData?>)

    @Query("SELECT * FROM weather_table")
    fun getWeatherFromLocal(): Flow<List<LocalWeatherData>>

}