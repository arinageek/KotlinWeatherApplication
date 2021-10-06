package com.example.kotlinweatherapplication.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName="cities_table")
data class City (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val isHome: Boolean

)
