package com.example.kotlinweatherapplication.repository

import com.example.kotlinweatherapplication.openweathermap.WeatherApi
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {
}