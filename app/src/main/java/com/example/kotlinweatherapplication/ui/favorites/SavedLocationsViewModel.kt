package com.example.kotlinweatherapplication.ui.favorites

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapplication.database.entities.City
import com.example.kotlinweatherapplication.networking.openweathermap.current_weather_models.CurrentWeatherResponse
import com.example.kotlinweatherapplication.repository.SavedCitiesRepository
import com.example.kotlinweatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SavedLocationsViewModel @Inject constructor(
    val app: Application,
    private val citiesRepository: SavedCitiesRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private var weatherList: MutableList<CurrentWeatherResponse> = mutableListOf()
    private var _weatherResponse: MutableLiveData<List<CurrentWeatherResponse>> = MutableLiveData()
    val weatherResponse: LiveData<List<CurrentWeatherResponse>>
    get() = _weatherResponse

    init{
        getCitiesFromDb()
    }

    fun getCitiesFromDb() = viewModelScope.launch {
        try{
            val response = citiesRepository.getAllCities()
            if(!response.isNullOrEmpty()) getWeatherForEachCity(response)
        }catch(ex: Exception){
            Log.d("SavedLocationsViewModel", ex.toString())
        }
    }

    fun getWeatherForEachCity(cities: List<City>) = viewModelScope.launch {
        for(city in cities){
            val response = weatherRepository.getCurrentWeather(city.name)
            weatherList.add(response)
            _weatherResponse.postValue(weatherList)
        }
    }
}