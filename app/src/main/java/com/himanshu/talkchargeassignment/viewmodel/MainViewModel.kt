package com.himanshu.talkchargeassignment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.himanshu.talkchargeassignment.model.HourlyWeatherDetails
import com.himanshu.talkchargeassignment.model.WeatherData
import com.himanshu.talkchargeassignment.model.WeatherDetails
import com.himanshu.talkchargeassignment.services.WeatherAPIService
import com.himanshu.talkchargeassignment.utils.dayfromDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {


    private val weatherApiService = WeatherAPIService()
    private val disposable = CompositeDisposable()

    val weatherData = MutableLiveData<WeatherData>()
    val error = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun getDataFromAPI(lat: Double, lon: Double, appid: String) {

        loading.value = true
        disposable.add(
            weatherApiService.getDataService(lat, lon, appid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<WeatherData>() {

                    override fun onSuccess(t: WeatherData) {
                        weatherData.value = t
                        error.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        Log.e("WeatherData", e.toString())
                        error.value = true
                        loading.value = false
                    }

                })
        )

    }

    fun getWeatherDetails(): List<WeatherDetails> {
        val ans = ArrayList<WeatherDetails>()
        for (i in 0..7) {
            val date = Date()
            date.date = date.date + i
            val d = if (date.date < 10) "0${date.date}" else date.date
            val mon = if (date.month + 1 < 10) "0${date.month + 1}" else date.month + 1
            ans.add(
                WeatherDetails(
                    "${d}/${mon}",
                    dayfromDate(date.day),
                    "sun",
                    (32..40).random().toDouble(),
                    (26..32).random().toDouble()
                )
            )
        }
        return ans
    }


    fun getHourlyWeatherDetails(): List<HourlyWeatherDetails> {
        val ans = ArrayList<HourlyWeatherDetails>()
        val date = Date()
        Log.e("HOur", date.hours.toString() )
        for (i in date.hours..23) {
            val hour = if(i < 10) "0${i}:30" else "${i}:30"
            ans.add(HourlyWeatherDetails(hour,"sun",(29..40).random().toString()))
        }
        return ans
    }
}