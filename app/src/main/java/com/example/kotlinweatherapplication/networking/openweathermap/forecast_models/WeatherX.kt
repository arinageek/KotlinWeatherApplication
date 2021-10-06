package com.example.kotlinweatherapplication.networking.openweathermap.forecast_models

data class WeatherX(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)