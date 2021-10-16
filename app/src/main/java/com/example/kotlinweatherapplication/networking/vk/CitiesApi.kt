package com.example.kotlinweatherapplication.networking.vk

import com.example.kotlinweatherapplication.Utils.Constants.VK_API_KEY
import com.example.kotlinweatherapplication.Utils.Constants.VK_BASE_URL
import com.example.kotlinweatherapplication.networking.vk.cities_models.CitiesResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CitiesApi {
    @GET(VK_BASE_URL)
    suspend fun getCities(@Query("v") version: String = "5.81", @Query("lang") lang: String = "en",
                          @Query("access_token") key: String = VK_API_KEY, @Query("q") query: String,
                          @Query("count") count: String = "20", @Query("country_id") countryId: String = "1",
                          @Query("need_all") needAll: String = "0")
    : CitiesResponse
}