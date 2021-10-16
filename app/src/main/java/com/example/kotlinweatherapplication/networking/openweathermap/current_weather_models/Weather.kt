package com.example.kotlinweatherapplication.networking.openweathermap.current_weather_models

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)