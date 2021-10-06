package com.example.kotlinweatherapplication.repository

import com.example.kotlinweatherapplication.networking.openweathermap.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {

    suspend fun getWeatherForecast(lat: Double, lon: Double) = api.getWeatherForecast(lat = lat, lon = lon);

}