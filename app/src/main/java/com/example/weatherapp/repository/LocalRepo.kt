package com.example.weatherapp.repository

import android.content.Context
import android.util.Log
import com.example.weatherapp.local.LocalWeatherData
import com.example.weatherapp.local.WeatherDB
import com.example.weatherapp.utility.Resource
import kotlinx.coroutines.flow.*

class LocalRepo {

    suspend fun getWeatherLocal(context: Context): Flow<Resource<List<LocalWeatherData>>> {
        val dao = WeatherDB.getInstance(context).getDao()
        val data = dao.getWeatherFromLocal()
        val distinctData = data.distinctUntilChanged()
        return flow { emitAll(distinctData.map { Resource.Success(it) }) }
    }

}