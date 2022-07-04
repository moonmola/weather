package com.moonmola.weather.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather")
data class WeatherResponse (
    @SerializedName("lat") val lat : Double,
    @SerializedName("lon") val lon : Double,
    @PrimaryKey @SerializedName("timezone") val timezone : String,
    @SerializedName("timezone_offset") val timezone_offset : Int,
    @SerializedName("hourly") val hourly : List<HourWeather>,
    @SerializedName("daily") val daily : List<DayWeather>,
    @SerializedName("current") val current : HourWeather
)