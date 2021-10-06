package com.example.kotlinweatherapplication.networking.openweathermap

import com.example.kotlinweatherapplication.Utils.Constants.WEATHER_API_KEY
import com.example.kotlinweatherapplication.networking.openweathermap.forecast_models.ForecastWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/onecall?")
    suspend fun getWeatherForecast(@Query("lat") lat: Double, @Query("lon") lon: Double,
                                   @Query("exclude") exclude: String = "minutes",
                                   @Query("units") units: String = "metric",
                                   @Query("appid") appId: String = WEATHER_API_KEY)
    : ForecastWeatherResponse

}