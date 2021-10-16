package com.example.kotlinweatherapplication.repository

import com.example.kotlinweatherapplication.database.CityDao
import com.example.kotlinweatherapplication.database.entities.City
import com.example.kotlinweatherapplication.networking.openweathermap.GeocodingApi
import com.example.kotlinweatherapplication.networking.openweathermap.WeatherApi
import com.example.kotlinweatherapplication.networking.vk.CitiesApi
import javax.inject.Inject

class SavedCitiesRepository @Inject constructor(
    private val cityDao: CityDao
) {
    suspend fun insertCity(city: City) = cityDao.insert(city)
    suspend fun deleteCity(city: City) = cityDao.delete(city)
    suspend fun getAllCities() = cityDao.getAllCities()
}