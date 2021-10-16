package com.example.kotlinweatherapplication.di

import android.content.Context
import androidx.room.Room
import com.example.kotlinweatherapplication.Utils.Constants.BASE_URL
import com.example.kotlinweatherapplication.Utils.Constants.VK_BASE_URL
import com.example.kotlinweatherapplication.database.CityDao
import com.example.kotlinweatherapplication.database.CityDatabase
import com.example.kotlinweatherapplication.networking.openweathermap.GeocodingApi
import com.example.kotlinweatherapplication.networking.openweathermap.WeatherApi
import com.example.kotlinweatherapplication.networking.vk.CitiesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

    @Singleton
    @Provides
    fun provideCitiesApi(retrofit: Retrofit): CitiesApi =
        retrofit.create(CitiesApi::class.java)

    @Singleton
    @Provides
    fun provideGeocodingApi(retrofit: Retrofit): GeocodingApi =
        retrofit.create(GeocodingApi::class.java)

    @Provides
    @Singleton
    fun provideCityDatabase(@ApplicationContext appContext: Context): CityDatabase =
        Room.databaseBuilder(
            appContext,
            CityDatabase::class.java,
            "city_database.db"
        ).build()

    @Provides
    @Singleton
    fun provideCityDao(db: CityDatabase): CityDao = db.getDao()

 }