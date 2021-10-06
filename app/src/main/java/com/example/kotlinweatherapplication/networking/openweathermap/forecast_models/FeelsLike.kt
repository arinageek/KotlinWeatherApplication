package com.example.kotlinweatherapplication.networking.openweathermap.forecast_models

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)