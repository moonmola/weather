package com.moonmola.weather.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.moonmola.weather.data.HourWeather

@ProvidedTypeConverter
class HourWeatherTypeConverter(private val gson: Gson) {
    @TypeConverter
    fun listToJson(value: HourWeather): String? {
        return gson.toJson(value)
    }
    @TypeConverter
    fun jsonToList(value: String): HourWeather {
        return gson.fromJson(value, HourWeather::class.java)
    }
}
