package com.example.kotlinweatherapplication.networking.openweathermap.forecast_models

data class ForecastWeatherResponse(
    val alerts: List<Alert>,
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Double
)