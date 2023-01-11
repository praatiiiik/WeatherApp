package com.example.weatherapp

import android.app.Application
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.local.LocalWeatherData
import com.example.weatherapp.repository.MainRepo
import com.example.weatherapp.utility.Resource
import com.example.weatherapp.utility.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application):AndroidViewModel(application) {

    private val mainRepo : MainRepo = MainRepo()
    private val _weather : MutableStateFlow<Status<List<LocalWeatherData>>?> = MutableStateFlow(Status.empty())
    val weather : StateFlow<Status<List<LocalWeatherData>>?> = _weather.asStateFlow()

    fun getWeatherDataFromServer(lat:Double?,lon:Double?){
        getWeatherDataLocally()
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.getWeatherDataFromServer(lat, lon, getApplication())
        }
    }

    private fun getWeatherDataLocally(){
        viewModelScope.launch(Dispatchers.IO) {
            val data = mainRepo.getWeatherLocally(getApplication())
            data.onStart {
                _weather.value = Status.loading()
            }.map { resource -> Status.fromResource(resource) }
                .collect { state ->
                    _weather.value = state
                    Log.d("resValue",state.toString())
                }
        }
    }

}