package com.example.kotlinweatherapplication.ui.home

import androidx.lifecycle.ViewModel
import com.example.kotlinweatherapplication.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

}