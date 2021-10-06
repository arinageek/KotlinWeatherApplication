package com.example.kotlinweatherapplication.networking.openweathermap.forecast_models

data class Hourly(
    val clouds: Double,
    val dew_point: Double,
    val dt: Long,
    val feels_like: Double,
    val humidity: Double,
    val pop: Double,
    val pressure: Double,
    val temp: Double,
    val uvi: Double,
    val visibility: Double,
    val weather: List<WeatherXX>,
    val wind_deg: Double,
    val wind_gust: Double,
    val wind_speed: Double
)