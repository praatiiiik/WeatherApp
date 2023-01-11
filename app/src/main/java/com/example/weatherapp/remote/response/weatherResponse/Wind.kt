package com.example.weatherapp.remote.response.weatherResponse

import retrofit2.Response

data class Wind(
    val deg: Int?,
    val gust: Double?,
    val speed: Double?
)