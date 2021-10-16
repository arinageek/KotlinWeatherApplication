package com.example.kotlinweatherapplication.ui.home

import com.example.kotlinweatherapplication.networking.vk.cities_models.Item

interface CitiesOnItemClickListener {
    fun onItemClick(city: Item)
}