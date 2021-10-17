package com.example.kotlinweatherapplication.ui.home

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
import com.example.kotlinweatherapplication.networking.openweathermap.forecast_models.ForecastWeatherResponse
import com.example.kotlinweatherapplication.networking.vk.cities_models.CitiesResponse
import com.example.kotlinweatherapplication.repository.SavedCitiesRepository
import com.example.kotlinweatherapplication.repository.WeatherRepository
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val app: Application,
    private val weatherRepository: WeatherRepository,
    private val citiesRepository: SavedCitiesRepository
) : ViewModel() {

    private val TAG = "HomeViewModel"

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    private var _weatherResponse: MutableLiveData<ForecastWeatherResponse> = MutableLiveData()
    val weatherResponse: LiveData<ForecastWeatherResponse>
        get() = _weatherResponse

    private var _citiesResponse: MutableLiveData<CitiesResponse> = MutableLiveData()
    val citiesResponse: LiveData<CitiesResponse>
        get() = _citiesResponse

    private var _isAlreadySaved: MutableLiveData<Boolean> = MutableLiveData()
    val isAlreadySaved: LiveData<Boolean>
        get() = _isAlreadySaved

    var currentCity: String = "Moscow"


    init {
        getWeatherForecast()
    }

    fun insertCity() = viewModelScope.launch {
        if (!currentCity.isNullOrBlank()) {
            citiesRepository.insertCity(City(name = currentCity))
            _isAlreadySaved.postValue(true)
        }
    }

    fun getWeatherForecast(lat: Double = 55.7522, lon: Double = 37.6156) = viewModelScope.launch {
        if (isConnectedToInternet()) {
            eventChannel.send(Event.removeNoInternetConnectionMessage)
            try {
                val weather = weatherRepository.getWeatherForecast(lat = lat, lon = lon)
                _weatherResponse.postValue(weather)
                _isAlreadySaved.postValue(false)
            } catch (t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        } else {
            eventChannel.send(Event.showNoInternetConnectionMessage)
        }
    }

    fun getCities(query: String) = viewModelScope.launch {
        if (isConnectedToInternet()) {
            eventChannel.send(Event.removeNoInternetConnectionMessage)
            try {
                val response = weatherRepository.getCities(query)
                _citiesResponse.postValue(response)
            } catch (t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        } else {
            eventChannel.send(Event.showNoInternetConnectionMessage)
        }
    }

    fun getCityCoordinates(city: String) = viewModelScope.launch {
        if (isConnectedToInternet()) {
            eventChannel.send(Event.removeNoInternetConnectionMessage)
            try {
                val response = weatherRepository.getCityCoordinates(city + ",ru")
                response?.let {
                    currentCity = city
                    getWeatherForecast(lat = response[0].lat, lon = response[0].lon)
                }
            } catch (t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        } else {
            eventChannel.send(Event.showNoInternetConnectionMessage)
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

    sealed class Event {
        object showNoInternetConnectionMessage : Event()
        object removeNoInternetConnectionMessage : Event()
    }

}