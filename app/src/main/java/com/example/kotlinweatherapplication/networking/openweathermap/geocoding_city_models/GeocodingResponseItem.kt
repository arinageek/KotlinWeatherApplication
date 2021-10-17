package com.example.kotlinweatherapplication.networking.openweathermap.geocoding_city_models

data class GeocodingResponseItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String
)