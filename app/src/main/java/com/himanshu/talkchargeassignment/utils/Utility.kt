package com.himanshu.talkchargeassignment.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.himanshu.talkchargeassignment.utils.KEYS.REQUEST_CHECK_SETTINGS

fun Context.toast(msg:String)= Toast.makeText(this,msg,Toast.LENGTH_LONG).show()


fun Activity.checkLocationPermission(): Boolean {
    val accessFineLocation =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    val accessCourseLocation =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

    val listPermissionNeeded = ArrayList<String>()

    if (accessFineLocation != PackageManager.PERMISSION_GRANTED) {
        listPermissionNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    if (accessCourseLocation != PackageManager.PERMISSION_GRANTED) {
        listPermissionNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    val permissionRequest = arrayOfNulls<String>(listPermissionNeeded.size)
    for (i in listPermissionNeeded.indices) {
        permissionRequest[i] = listPermissionNeeded[i]
    }

    if (listPermissionNeeded.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            this,
            permissionRequest,
            KEYS.LOCATION_PERMISSION
        )
        return false
    }
    return true

}

fun Context.isGPSEnable():Boolean {
    val locationManager = (this.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Activity.turnOnGPS() : Boolean{
    val GpsIsOn = arrayOf(false)
    val locationRequest = LocationRequest.create()
    locationRequest.interval = 5000
    locationRequest.fastestInterval = 1000
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val client = LocationServices.getSettingsClient(this.applicationContext)
    val task = client.checkLocationSettings(builder.build())

    task.addOnSuccessListener(this,
        OnSuccessListener {
            GpsIsOn[0] = true
        })

    task.addOnFailureListener(this, OnFailureListener { e: Exception? ->
        GpsIsOn[0] = false
        if (e is ResolvableApiException) {
            try {
                val resolvable = e as ResolvableApiException
                resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
            } catch (sendEx: SendIntentException) {
                // Ignore the error.
            }
        }
    })
    return GpsIsOn[0]
}

fun dayfromDate( date:Int) : String{
  return  when(date) {
        0-> "Sun"
        1-> "Mon"
        2-> "Tue"
        3-> "Wed"
        4-> "Thu"
        5-> "Fri"
        6-> "Sat"
        else -> "NA"
    }
}