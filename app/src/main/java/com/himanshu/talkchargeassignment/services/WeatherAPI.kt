package com.himanshu.talkchargeassignment.services

import com.himanshu.talkchargeassignment.model.WeatherData
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {


    @GET("data/2.5/weather")
    fun getData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): Single<WeatherData>

}