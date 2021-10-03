package com.example.kotlinweatherapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapplication.openweathermap.forecast_models.ForecastWeatherResponse
import com.example.kotlinweatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private var _weatherResponse: MutableLiveData<ForecastWeatherResponse> = MutableLiveData()
    val weatherResponse: LiveData<ForecastWeatherResponse>
    get() = _weatherResponse

    init {
        getWeatherForecast()
    }

    fun getWeatherForecast() = viewModelScope.launch {
        val weather = repository.getWeatherForecast(55.7522,37.6156 )
        _weatherResponse.postValue(weather)
    }

}