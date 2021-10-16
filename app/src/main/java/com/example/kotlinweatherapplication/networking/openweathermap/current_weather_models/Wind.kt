package com.example.kotlinweatherapplication.networking.openweathermap.current_weather_models

data class Wind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)