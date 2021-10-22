package com.example.kotlinweatherapplication.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapplication.database.DataStoreManager
import com.example.kotlinweatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStoreManager,
    private val repository: WeatherRepository
) : ViewModel() {

    private var _citiesResponse: MutableLiveData<List<String>> = MutableLiveData()
    val citiesResponse: LiveData<List<String>>
        get() = _citiesResponse

    var savedCountry: MutableLiveData<String> = MutableLiveData()
    var savedCity: MutableLiveData<String> = MutableLiveData()

    private var currentCountry: String = ""
    private var currentCountryId: Int = -1
    private var currentCity: String = ""

    private val eventChannel = Channel<SettingsEvent>()
    val event = eventChannel.receiveAsFlow()

    init {
        getDataFromDb()
    }

    fun getCities(query: String) = viewModelScope.launch {
        if (currentCountryId == -1) return@launch
        val cities = repository.getCities(query = query, countryId = currentCountryId)
        val listOfCityNames = cities.response.items.map { item -> item.title }
        _citiesResponse.postValue(listOfCityNames)
    }

    fun getDataFromDb() = viewModelScope.launch {

        //country
        val country = dataStore.getCountryName()
        savedCountry.postValue(country)

        //city
        val city = dataStore.getCityName()
        savedCity.postValue(city)
    }

    fun saveCountry(name: String, id: Int) {
        currentCountryId = id
        currentCountry = name
    }

    fun saveCity(name: String) {
        currentCity = name
    }

    fun saveDataToDb() = viewModelScope.launch {
        if (currentCity.isNullOrBlank() || currentCountry.isNullOrBlank() || currentCountryId == -1) {
            eventChannel.send(SettingsEvent.ShowIncorrectDataNotification)
            return@launch
        }
        dataStore.setCountryId(currentCountryId)
        dataStore.setCountryName(currentCountry)
        dataStore.setCityName(currentCity)
    }

    sealed class SettingsEvent {
        object ShowIncorrectDataNotification : SettingsEvent()
    }

}