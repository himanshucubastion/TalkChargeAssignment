package com.himanshu.talkchargeassignment.model

data class WeatherDetails(
    val date: String,
    val day: String,
    val icon: String,
    val maxTemp: Double,
    val minTemp: Double
)