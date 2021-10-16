package com.example.kotlinweatherapplication.repository

import com.example.kotlinweatherapplication.networking.openweathermap.GeocodingApi
import com.example.kotlinweatherapplication.networking.openweathermap.WeatherApi
import com.example.kotlinweatherapplication.networking.vk.CitiesApi
import javax.inject.Inject

class Repository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val citiesApi: CitiesApi,
    private val geocodingApi: GeocodingApi
) {

    suspend fun getWeatherForecast(lat: Double, lon: Double) = weatherApi.getWeatherForecast(lat = lat, lon = lon)
    suspend fun getCities(query: String) = citiesApi.getCities(query = query)
    suspend fun getCityCoordinates(query: String) = geocodingApi.getCityCoordinates(query = query)

}