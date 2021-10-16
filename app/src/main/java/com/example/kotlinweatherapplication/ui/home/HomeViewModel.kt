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
import com.example.kotlinweatherapplication.networking.openweathermap.forecast_models.ForecastWeatherResponse
import com.example.kotlinweatherapplication.networking.vk.cities_models.CitiesResponse
import com.example.kotlinweatherapplication.repository.Repository
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val app: Application,
    private val repository: Repository
) : ViewModel() {

    private val TAG = "HomeViewModel"

    private val eventChannel = Channel<WeatherEvent>()
    val event = eventChannel.receiveAsFlow()

    private var _weatherResponse: MutableLiveData<ForecastWeatherResponse> = MutableLiveData()
    val weatherResponse: LiveData<ForecastWeatherResponse>
    get() = _weatherResponse

    private var _citiesResponse: MutableLiveData<CitiesResponse> = MutableLiveData()
    val citiesResponse: LiveData<CitiesResponse>
        get() = _citiesResponse


    init {
        getWeatherForecast()
    }

    fun getWeatherForecast() = viewModelScope.launch {
        if(isConnectedToInternet()) {
            viewModelScope.launch { eventChannel.send(WeatherEvent.removeNoInternetConnectionMessage) }
            try{
                val weather = repository.getWeatherForecast(55.7522,37.6156 )
                _weatherResponse.postValue(weather)
            }catch(t: Throwable){
                Log.d(TAG, t.message.toString())
            }
        }else{
            viewModelScope.launch { eventChannel.send(WeatherEvent.showNoInternetConnectionMessage) }
        }
    }

    fun getCities(query: String) = viewModelScope.launch {
        try{
            val response = repository.getCities(query)
            _citiesResponse.postValue(response)
            //Log.d(TAG, response.toString())
        }catch(t: Throwable){
            Log.d(TAG, t.message.toString())
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

    sealed class WeatherEvent {
        object showNoInternetConnectionMessage: WeatherEvent()
        object removeNoInternetConnectionMessage: WeatherEvent()
    }

}