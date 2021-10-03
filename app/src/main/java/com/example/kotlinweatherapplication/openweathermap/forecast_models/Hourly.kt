package com.example.kotlinweatherapplication.openweathermap.forecast_models

data class Hourly(
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pop: Int,
    val pressure: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<WeatherXX>,
    val wind_deg: Int,
    val wind_gust: Double,
    val wind_speed: Double
)