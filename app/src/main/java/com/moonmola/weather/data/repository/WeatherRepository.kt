package com.moonmola.weather.data.repository

import com.moonmola.weather.data.WeatherResponse
import com.moonmola.weather.data.db.WeatherDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor (private val weatherDAO: WeatherDAO) {
    fun getWeather() = weatherDAO.getWeather()
    suspend fun insertWeather(weatherResponse: WeatherResponse) {
        weatherDAO.insert(weatherResponse)
    }
    companion object {

        // For Singleton instantiation
        @Volatile private var instance: WeatherRepository? = null

        fun getInstance(weatherDAO: WeatherDAO) =
            instance ?: synchronized(this) {
                instance ?: WeatherRepository(weatherDAO).also { instance = it }
            }
    }
}