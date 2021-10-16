package com.example.kotlinweatherapplication.repository

import com.example.kotlinweatherapplication.database.CityDao
import com.example.kotlinweatherapplication.database.entities.City
import com.example.kotlinweatherapplication.networking.openweathermap.GeocodingApi
import com.example.kotlinweatherapplication.networking.openweathermap.WeatherApi
import com.example.kotlinweatherapplication.networking.vk.CitiesApi
import javax.inject.Inject

class Repository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val citiesApi: CitiesApi,
    private val geocodingApi: GeocodingApi,
    private val cityDao: CityDao
) {

    suspend fun getWeatherForecast(lat: Double, lon: Double) = weatherApi.getWeatherForecast(lat = lat, lon = lon)
    suspend fun getCities(query: String) = citiesApi.getCities(query = query)
    suspend fun getCityCoordinates(query: String) = geocodingApi.getCityCoordinates(query = query)
    suspend fun insertCity(city: City) = cityDao.insert(city)
    suspend fun deleteCity(city: City) = cityDao.delete(city)

}