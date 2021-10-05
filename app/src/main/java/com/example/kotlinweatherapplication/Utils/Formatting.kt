package com.example.kotlinweatherapplication.Utils

import java.text.SimpleDateFormat

object Formatting {

    fun getDate(dt: Long): String {
        val sd = SimpleDateFormat("dd-MMM")
        return sd.format(dt * 1000)
    }

    fun getFullDate(dt: Long): String {
        val sd = SimpleDateFormat("d MMM HH:mm")
        return sd.format(dt * 1000)
    }

    fun getTemp(temp: Double): String = temp.toInt().toString()+"°C"

}