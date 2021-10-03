package com.example.kotlinweatherapplication.openweathermap.forecast_models

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)