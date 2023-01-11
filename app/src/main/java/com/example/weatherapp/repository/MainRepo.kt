package com.example.weatherapp.repository

import android.content.Context
import com.example.weatherapp.local.LocalWeatherData
import com.example.weatherapp.utility.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Single repo to communicate with local data and server
 * Using single source of truth pattern for data streaming
 */
class MainRepo {
    //fetch data and save to local db
    suspend fun getWeatherDataFromServer(lat:Double?,lon:Double?,context: Context){
        val remoteRepo = RemoteRepo()
        remoteRepo.getWeatherDataFromServer(lat, lon, context)
    }

    //get db data
    suspend fun getWeatherLocally(context: Context):Flow<Resource<List<LocalWeatherData>>>{
        val localRepo = LocalRepo()
        return localRepo.getWeatherLocal(context)
    }
}