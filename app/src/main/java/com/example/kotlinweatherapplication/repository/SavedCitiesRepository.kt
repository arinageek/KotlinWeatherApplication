package com.example.kotlinweatherapplication.repository

import com.example.kotlinweatherapplication.database.CityDao
import com.example.kotlinweatherapplication.database.entities.City
import javax.inject.Inject

class SavedCitiesRepository @Inject constructor(
    private val cityDao: CityDao
) {
    suspend fun insertCity(city: String) = cityDao.insert(City(name = city))
    suspend fun deleteCity(city: String) = cityDao.delete(City(name = city))
    suspend fun getAllCities() = cityDao.getAllCities()
}