package com.moonmola.weather.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.moonmola.weather.data.DayWeather
import com.moonmola.weather.data.HourWeather

@ProvidedTypeConverter
class DayWeatherTypeConverter(private val gson: Gson) {
    @TypeConverter
    fun listToJson(value: List<DayWeather>): String? {
        return gson.toJson(value)
    }
    @TypeConverter
    fun jsonToList(value: String): List<DayWeather> {
        return gson.fromJson(value, Array<DayWeather>::class.java).toList()
    }
}
