package com.moonmola.weather.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.moonmola.weather.data.WeatherResponse
import com.moonmola.weather.data.WeatherAPIService
import com.moonmola.weather.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val weatherAPIService: WeatherAPIService
) : ViewModel() {
    var stateWeatherInfo = MutableLiveData<WeatherResponse?>()
    fun getLocalWeather() = viewModelScope.launch(Dispatchers.IO) {
        stateWeatherInfo.postValue(weatherRepository.getWeather())
        Log.e("Dabong.Local", weatherRepository.getWeather().toString())

    }
    fun getRemoteWeatherData(lat: Double, lon: Double) = viewModelScope.launch(Dispatchers.IO) {
        val result  = weatherAPIService.stateWeatherData(lat, lon)
        result.body()?.let {
            Log.e("Dabong.Remote", it.toString())
            weatherRepository.insertWeather(it)
            stateWeatherInfo.postValue(it)
        }
    }


}