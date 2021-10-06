package com.example.kotlinweatherapplication.networking.vk.cities_models

data class Response(
    val count: Int,
    val items: List<Item>
)