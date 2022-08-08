package com.himanshu.talkchargeassignment.services

import com.himanshu.talkchargeassignment.model.WeatherData
import com.himanshu.talkchargeassignment.model.WeatherDetails
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class WeatherAPIService {


    private val BASE_URL = "https://api.openweathermap.org/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

    fun getDataService(lat: Double, lon: Double, appid: String): Single<WeatherData> {
        return api.getData(lat, lon, appid)
    }



}