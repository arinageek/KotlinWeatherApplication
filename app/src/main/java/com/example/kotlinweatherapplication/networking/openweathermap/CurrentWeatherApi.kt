package com.example.kotlinweatherapplication.networking.openweathermap

import com.example.kotlinweatherapplication.Utils.Constants.WEATHER_API_KEY
import com.example.kotlinweatherapplication.networking.openweathermap.current_weather_models.CurrentWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherApi {

    @GET("data/2.5/weather?")
    suspend fun getCurrentWeather(@Query("q") query: String,
                                  @Query("lang") lang: String = "ru",
                                  @Query("units") units: String = "metric",
                                  @Query("appid") appId: String = WEATHER_API_KEY)
    : CurrentWeatherResponse

}