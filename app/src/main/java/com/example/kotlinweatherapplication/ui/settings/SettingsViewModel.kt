package com.example.kotlinweatherapplication.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinweatherapplication.database.DataStoreManager
import com.example.kotlinweatherapplication.networking.vk.cities_models.CitiesResponse
import com.example.kotlinweatherapplication.networking.vk.countries_models.CountriesResponse
import com.example.kotlinweatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStoreManager,
    private val repository: WeatherRepository
) : ViewModel() {

    private var listOfCountryIds: List<Int> = listOf()

    private var _citiesResponse: MutableLiveData<List<String>> = MutableLiveData()
    val citiesResponse: LiveData<List<String>>
        get() = _citiesResponse

    private var _countriesResponse: MutableLiveData<List<String>> = MutableLiveData()
    val countriesResponse: LiveData<List<String>>
        get() = _countriesResponse

    private var _countrySelected: MutableLiveData<Boolean> = MutableLiveData()
    val countrySelected: LiveData<Boolean>
        get() = _countrySelected

    private var countryId: Int = -1

    init {
        getCountries()
    }

    fun getCountries() = viewModelScope.launch {
        val countries = repository.getCountries()
        val listOfCountryNames = countries.response.items.map { item -> item.title }
        listOfCountryIds = countries.response.items.map { item -> item.id }
        _countriesResponse.postValue(listOfCountryNames)
    }

    fun getCities(query: String) = viewModelScope.launch {
        if(countryId == -1) return@launch
        val cities = repository.getCities(query = query, countryId)
        val listOfCityNames = cities.response.items.map { item -> item.title }
        _citiesResponse.postValue(listOfCityNames)
    }

    fun saveCountry(position: Int) = viewModelScope.launch {
        if (countriesResponse.value.isNullOrEmpty()) return@launch
        countryId = listOfCountryIds.get(position)
        dataStore.setCountryId(countryId)
        dataStore.setCountryName(countriesResponse.value?.get(position)!!)
        _countrySelected.postValue(true)
    }

}