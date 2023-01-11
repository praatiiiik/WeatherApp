package com.example.weatherapp.utility

//sealed class for data layer
//no need to check data with if else
sealed class Resource<T>{
    data class Success<T>(val data : T): Resource<T>()
    data class Failed<T>(val msg : String): Resource<T>()
}