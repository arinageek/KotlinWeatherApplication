package com.example.kotlinweatherapplication.networking.openweathermap.current_weather_models

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)