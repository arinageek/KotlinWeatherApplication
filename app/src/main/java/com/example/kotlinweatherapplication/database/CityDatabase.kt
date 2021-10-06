package com.example.kotlinweatherapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlinweatherapplication.database.entities.City

@Database(entities = [City::class], version = 1)
abstract class CityDatabase : RoomDatabase() {
    abstract fun getDao(): CityDao
}