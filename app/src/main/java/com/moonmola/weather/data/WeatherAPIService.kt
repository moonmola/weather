package com.moonmola.weather.data

import com.moonmola.weather.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {

    //https://api.openweathermap.org/data/3.0/onecall?lat={lat}&lon={lon}&exclude={part}&appid={API key}

    @GET("onecall")
    suspend fun stateWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
//        @Query("exclude") exclude:String = BuildConfig.EXCLUSION,
        @Query("appid") appid: String = BuildConfig.API_KEY
    ): Response<WeatherResponse>

    //@Query("units") units: String = BuildConfig.TEMPERATURE,

}