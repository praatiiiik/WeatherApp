package com.example.weatherapp.repository

import android.content.Context
import com.example.weatherapp.local.LocalWeatherData
import com.example.weatherapp.local.WeatherDB
import com.example.weatherapp.remote.Networking
import com.example.weatherapp.utility.Constants

class RemoteRepo {

    suspend fun getWeatherDataFromServer(lat:Double?, lon:Double?,context: Context){
        val list = ArrayList<LocalWeatherData?>()
        val retrofit = Networking.instance.getRetrofit()
        for(loc in getLOCList(lat,lon)){
            if(loc?.lat==null || loc.lon==null){
                list.add(LocalWeatherData(loc!!.location, null))
                continue
            }
            loc.let {
                it.apply {
                    val weatherResponse = retrofit.getWeather(this.lat!!, this.lon!!)
                    if(weatherResponse.isSuccessful && weatherResponse.body()!=null){
                        val data = LocalWeatherData(location, weatherResponse.body())
                        list.add(data)
                    }
                }
            }
        }
        WeatherDB.getInstance(context).getDao().insertWeather(list)
    }

    private fun getLOCList(lat:Double?,lon:Double?):List<LOCATION?>{
        val currLoc =LOCATION(lat,lon,Constants.CURRENT_LOCATION)
        val mumbai = LOCATION(18.9387711,72.8353355,Constants.MUMBAI)
        val delhi = LOCATION(28.7040592,77.1024892,Constants.DELHI)
        val sydney = LOCATION(-33.8688,151.2093,Constants.SYDNEY)
        val melbourne = LOCATION(-37.813628,144.963058,Constants.MELBOURNE)
        val newYork = LOCATION(40.730610,-73.935242,Constants.NEW_YORK)
        val singapore = LOCATION(1.352083,103.819836,Constants.SINGAPORE)

        return listOf(currLoc,mumbai,delhi,sydney,melbourne,newYork,singapore)
    }

    data class LOCATION(val lat:Double?, val lon:Double?, val location:String)
}