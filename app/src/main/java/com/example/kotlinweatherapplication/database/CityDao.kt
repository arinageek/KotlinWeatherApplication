package com.example.kotlinweatherapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.kotlinweatherapplication.database.entities.City

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: City)

    @Delete
    suspend fun delete(city: City)

    @Transaction
    @Query("DELETE FROM cities_table")
    suspend fun deleteAllCities()

    @Query("SELECT * FROM cities_table")
    suspend fun getAllCities(): List<City>

}