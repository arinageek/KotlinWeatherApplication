package com.example.kotlinweatherapplication.networking.openweathermap

import com.example.kotlinweatherapplication.Utils.Constants.WEATHER_API_KEY
import com.example.kotlinweatherapplication.networking.openweathermap.geocoding_city_models.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("geo/1.0/direct?")
    suspend fun getCityCoordinates(@Query("q") query: String,
                                   @Query("limit") limit: Int = 1,
                                   @Query("appid") appId: String = WEATHER_API_KEY)
    : GeocodingResponse

}