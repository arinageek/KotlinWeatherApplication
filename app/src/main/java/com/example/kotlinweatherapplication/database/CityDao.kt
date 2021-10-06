package com.example.kotlinweatherapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kotlinweatherapplication.database.entities.City

@Dao
interface CityDao {

    @Insert
    fun insert(city: City)

    @Update
    fun update(city: City)

    @Delete
    fun delete(city: City)

    @Transaction
    @Query("DELETE FROM cities_table")
    fun deleteAllCities()

    @Query("SELECT * FROM cities_table")
    fun getAllCities(): LiveData<List<City>>

}