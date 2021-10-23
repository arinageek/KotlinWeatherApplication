package com.example.kotlinweatherapplication.ui.favorites

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapplication.database.entities.City
import com.example.kotlinweatherapplication.networking.openweathermap.current_weather_models.CurrentWeatherResponse
import com.example.kotlinweatherapplication.repository.SavedCitiesRepository
import com.example.kotlinweatherapplication.repository.WeatherRepository
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SavedLocationsViewModel @Inject constructor(
    val app: Application,
    private val citiesRepository: SavedCitiesRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val TAG = "SavedLocationsViewModel"

    private val eventChannel = Channel<SavedLocationsEvent>()
    val event = eventChannel.receiveAsFlow()

    private var weatherList: MutableList<CurrentWeatherResponse> = mutableListOf()
    private var _weatherResponse: MutableLiveData<List<CurrentWeatherResponse>> = MutableLiveData()
    val weatherResponse: LiveData<List<CurrentWeatherResponse>>
        get() = _weatherResponse

    init {
        getCitiesFromDb()
    }

    fun deleteCity(city: CurrentWeatherResponse) = viewModelScope.launch {
        weatherList.remove(city)
        _weatherResponse.postValue(weatherList)
        citiesRepository.deleteCity(city.name)
    }

    fun getCitiesFromDb() = viewModelScope.launch {
        try {
            val response = citiesRepository.getAllCities()
            if (!response.isNullOrEmpty()) getWeatherForEachCity(response)
        } catch (ex: Exception) {
            Log.d(TAG, ex.toString())
        }
    }

    fun getWeatherForEachCity(cities: List<City>) = viewModelScope.launch {
        if (isConnectedToInternet()) {
            eventChannel.send(SavedLocationsEvent.removeNoInternetConnectionMessage)
            for (city in cities) {
                val response = weatherRepository.getCurrentWeather(city.name)
                response.name = city.name //otherwise deletion from db won't work properly
                weatherList.add(response)
                _weatherResponse.postValue(weatherList)
            }
        } else {
            eventChannel.send(SavedLocationsEvent.showNoInternetConnectionMessage)
        }
    }

    fun isConnectedToInternet(): Boolean {
        val cm = getApplication(app).getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = cm.activeNetwork ?: return false
            val capabilities = cm.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            cm.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

    sealed class SavedLocationsEvent {
        object showNoInternetConnectionMessage : SavedLocationsEvent()
        object removeNoInternetConnectionMessage : SavedLocationsEvent()
    }
}