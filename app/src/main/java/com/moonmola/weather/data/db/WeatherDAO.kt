package com.moonmola.weather.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moonmola.weather.data.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherResponse: WeatherResponse)

    @Query("SELECT * FROM weather")
    fun getWeatherList(): Flow<List<WeatherResponse>>

    @Query("SELECT * FROM weather")
    fun getWeather(): WeatherResponse?
}