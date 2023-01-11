package com.example.weatherapp.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherapp.remote.response.weatherResponse.WeatherResponse

@Entity(tableName = "weather_table")
data class LocalWeatherData(@PrimaryKey(autoGenerate = false) val location :String, @Embedded val weatherResponse: WeatherResponse?)
