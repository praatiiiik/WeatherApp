package com.example.weatherapp.remote.response.weatherResponse

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class WeatherResponse(
    val base: String?,
    @Embedded val clouds: Clouds?,
    val cod: Int?,
    @Embedded val coord: Coord?,
    val dt: Int?,
    @SerializedName("id") val responseId: Int?,
    @Embedded val main: Main?,
    val name: String?,
    @Embedded val sys: Sys?,
    val timezone: Int?,
    val visibility: Int?,
    @TypeConverters(WeatherTypeConverter::class) val weather: List<Weather>?,
    @Embedded val wind: Wind?
){
    @PrimaryKey(autoGenerate = true)
    var primaryKey : Int = 0
}

class WeatherTypeConverter{

    @TypeConverter // note this annotation
    fun fromOptionValuesList(optionValues: List<Weather>?): String? {
        if (optionValues == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Weather>?>() {}.type
        return gson.toJson(optionValues, type)
    }

    @TypeConverter // note this annotation
    fun toOptionValuesList(optionValuesString: String?): List<Weather>? {
        if (optionValuesString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object :
            TypeToken<List<Weather>>() {}.type
        return gson.fromJson(optionValuesString, type)
    }
}