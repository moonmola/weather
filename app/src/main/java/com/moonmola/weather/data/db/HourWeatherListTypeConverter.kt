package com.moonmola.weather.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.moonmola.weather.data.HourWeather

@ProvidedTypeConverter
class HourWeatherListTypeConverter(private val gson: Gson) {
    @TypeConverter
    fun listToJson(value: List<HourWeather>): String? {
        return gson.toJson(value)
    }
    @TypeConverter
    fun jsonToList(value: String): List<HourWeather> {
        return gson.fromJson(value, Array<HourWeather>::class.java).toList()
    }
}
