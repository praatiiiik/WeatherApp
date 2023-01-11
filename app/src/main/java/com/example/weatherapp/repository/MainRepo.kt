package com.example.weatherapp.repository

import android.content.Context
import com.example.weatherapp.local.LocalWeatherData
import com.example.weatherapp.utility.Resource
import kotlinx.coroutines.flow.Flow

class MainRepo {
    suspend fun getWeatherDataFromServer(lat:Double?,lon:Double?,context: Context){
        val remoteRepo = RemoteRepo()
        remoteRepo.getWeatherDataFromServer(lat, lon, context)
    }

    suspend fun getWeatherLocally(context: Context):Flow<Resource<List<LocalWeatherData>>>{
        val localRepo = LocalRepo()
        return localRepo.getWeatherLocal(context)
    }
}