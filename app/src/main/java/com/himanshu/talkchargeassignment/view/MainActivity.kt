package com.himanshu.talkchargeassignment.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.himanshu.talkchargeassignment.R
import com.himanshu.talkchargeassignment.databinding.ActivityMainBinding
import com.himanshu.talkchargeassignment.model.HourlyWeatherDetails
import com.himanshu.talkchargeassignment.model.WeatherDetails
import com.himanshu.talkchargeassignment.utils.*
import com.himanshu.talkchargeassignment.utils.KEYS.API_KEY
import com.himanshu.talkchargeassignment.utils.KEYS.REQUEST_CHECK_SETTINGS
import com.himanshu.talkchargeassignment.view.custom.Line
import com.himanshu.talkchargeassignment.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewmodel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private var client: FusedLocationProviderClient? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    viewmodel.getHourlyWeatherDetails()
        getLiveData()
        Log.e("WeatherDetails", viewmodel.getWeatherDetails().toString())

        if (checkLocationPermission()) {
            getUsersLocation()
        } else toast(" Please give the location permission")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                RESULT_OK -> {
                    getUsersLocation()
                    toast("Location ON")
                }
                RESULT_CANCELED -> {
                    toast("Please turn on your location")
                }
                else -> {}
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == KEYS.LOCATION_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUsersLocation()
        } else {
            toast("Please give the location permission")
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUsersLocation() {

        if (isGPSEnable()) {
            client = LocationServices.getFusedLocationProviderClient(this)
            client!!.lastLocation.addOnSuccessListener {
                it.let {
                    viewmodel.getDataFromAPI(it.latitude, it.longitude, API_KEY)
                }
            }
        } else turnOnGPS()

    }

    private fun getLiveData() {
        viewmodel.weatherData.observe(this) {
            it.let {
                binding.wtitle.text = it.weather[0].main
                binding.hum.text = it.main.humidity.toString()
                binding.perecivedTemperature.text = it.main.temp.toString()
                binding.eastWind.text = it.wind.speed.toString()
                binding.locName.text = it.name
                binding.visibility.text = it.visibility.toString()
                binding.pressure.text = it.main.pressure.toString()

                val listDate = viewmodel.getWeatherDetails()
                val hourlyDataList = viewmodel.getHourlyWeatherDetails()

                updateView(listDate,hourlyDataList)


            }
        }
    }

    private fun updateView(listDate: List<WeatherDetails>,hourlyDataList: List<HourlyWeatherDetails>) {
        val maxTempArr = ArrayList<Double>()
        val minTempArr = ArrayList<Double>()
        listDate.forEach {
            val view: View = layoutInflater.inflate(R.layout.layout_1, null)
            val date = view.findViewById<TextView>(R.id.date);
            date.text = "${it.day}\n${it.date}"

            binding.layout1.addView(view, setMargin(11))
            maxTempArr.add(it.maxTemp)
            minTempArr.add(it.minTemp)
        }

        hourlyDataList.forEach {
            val view: View = layoutInflater.inflate(R.layout.layout_2, null)
            val date = view.findViewById<TextView>(R.id.time)
            val temp = view.findViewById<TextView>(R.id.temp)

            date.text = it.time
            temp.text = "${it.temp}°"

            binding.layout2.addView(view,setMargin(15))

        }

        binding.line1.setMarginAbove(0.2)
        binding.line1.setMarginBelow(0.5)
        binding.line2.setMarginAbove(0.2)
        binding.line2.setMarginBelow(0.5)
        binding.line1.draw(maxTempArr);
        binding.line2.draw(minTempArr)

    }

    private fun setMargin(margin: Int): LinearLayout.LayoutParams? {
        val buttonLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonLayoutParams.setMargins(margin, margin, margin, margin)
        return buttonLayoutParams
    }


}