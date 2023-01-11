package com.example.weatherapp.remote

import com.example.weatherapp.remote.response.weatherResponse.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * getWeather function used to communicate with server
 * Suspend function for asynchronous task
 */
interface WeatherAPI {

    @GET(NetworkConstants.WEATHER_ENDPOINTS)
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ):Response<WeatherResponse>

}