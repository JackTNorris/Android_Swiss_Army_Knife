package com.example.android_swiss_army_knife.ui.speedometer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class SpeedometerViewModel(application: Application) : AndroidViewModel(application),
    LocationListener {

    private val _speed = MutableLiveData<String>().apply {
        value = "0"
    }
    val speed: LiveData<String>
        get() = _speed

    private var isBound = false

    fun bind(fragment: Fragment) {
        if (!isBound) {
            registerSensors()
            isBound = true
        }
    }

    @SuppressLint("StaticFieldLeak")
    private var context: Context

    init {
        context = application.applicationContext
    }


    private lateinit var sensorManager: SensorManager
    private lateinit var locationManager: LocationManager

    fun registerSensors() {
        // Get the SensorManager and LocationManager instances
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Register a LocationListener to receive location updates
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            this
        )
    }

    // Define a threshold for the minimum distance that indicates the user's location is changing
    private val MIN_DISTANCE_THRESHOLD = 1.0 // 1 meter

    // Store the previous location in a variable
    private var previousLocation: Location? = null


    override fun onLocationChanged(location: Location) {


        // Update the previous location with the current location
        previousLocation = location
        // Calculate the speed in meters per second
        val speed = location.speed

        // Convert the speed to kilometers per hour if needed
        val speedKmh = speed * 3.6

        // Convert the speed to miles per hour if needed
        var speedMph = speedKmh * 0.621371

        if (previousLocation != null) {
            // Calculate the distance between the current and previous locations
            val distance = location.distanceTo(previousLocation!!)

            // Check if the distance is below the MIN_DISTANCE_THRESHOLD
            if (distance < MIN_DISTANCE_THRESHOLD) {
                speedMph *= 0
            }
        }

        // Update the speed LiveData with the current speed
        _speed.value = speedMph.toInt().toString()
    }



    fun unregisterSensors() {
        // Unregister the LocationListener
        locationManager.removeUpdates(this)
    }

}
