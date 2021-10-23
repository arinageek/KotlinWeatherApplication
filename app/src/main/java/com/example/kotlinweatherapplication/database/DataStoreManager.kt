package com.example.kotlinweatherapplication.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager @Inject constructor(
    @ApplicationContext val context: Context
) {

    suspend fun getCountryId(): Int {
        val pref = intPreferencesKey("countryId")
        val preferences = context.dataStore.data.first()
        return preferences[pref] ?: 1
    }
    suspend fun getCountryName(): String {
        val pref = stringPreferencesKey("countryName")
        val preferences = context.dataStore.data.first()
        return preferences[pref] ?: "Россия"
    }
    suspend fun getCityName(): String {
        val pref = stringPreferencesKey("cityName")
        val preferences = context.dataStore.data.first()
        return preferences[pref] ?: "Москва"
    }
    suspend fun setCountryId(id: Int) {
        val pref = intPreferencesKey("countryId")
        context.dataStore.edit { settings ->
            settings[pref] = id
        }
    }
    suspend fun setCountryName(name: String) {
        val pref = stringPreferencesKey("countryName")
        context.dataStore.edit { settings ->
            settings[pref] = name
        }
    }
    suspend fun setCityName(name: String) {
        val pref = stringPreferencesKey("cityName")
        context.dataStore.edit { settings ->
            settings[pref] = name
        }
    }
}