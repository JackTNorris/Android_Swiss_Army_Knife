package com.example.android_swiss_army_knife.ui.avoid_ohio

import android.Manifest
import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.*
import com.example.android_swiss_army_knife.util.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices

class AvoidOhioViewModel(application: Application) : AndroidViewModel(application) {
    private val thisContext = getApplication<Application>().applicationContext

    //Boolean to keep track of whether permissions have been granted
    var locationPermissionEnabled:Boolean = false
    //Boolean to keep track of whether activity is currently requesting location Updates
    private var locationRequestsEnabled:Boolean = false
    //Member object for the FusedLocationProvider
    private lateinit var locationProviderClient: FusedLocationProviderClient
    //Member object for the last known location
    private lateinit var mCurrentLocation: Location
    //Member object to hold onto locationCallback object
    //Needed to remove requests for location updates
    private lateinit var mLocationCallback: LocationCallback

    // Launcher for locationPermissions. To be launched when location permissions
    // are not available
    lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>

    //LocationUtilCallback object
    //Dynamically defining two results from locationUtils
    //Namely requestPermissions and locationUpdated
    private val locationUtilCallback = object:LocationUtilCallback{
        //If locationUtil request fails because of permission issues
        //Ask for permissions
        override fun requestPermissionCallback() {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }
        //If locationUtil returns a Location object
        //Populate the current location and log
        override fun locationUpdatedCallback(location: Location) {
            mCurrentLocation = location
            _text.value = "Location is [Lat: ${location.latitude}, Long: ${location.longitude}]"
        }
    }

    fun startLocationRequests(){
        //If we aren't currently getting location updates
        if(!locationRequestsEnabled){
            //create a location callback
            mLocationCallback = createLocationCallback(locationUtilCallback)
            //and request location updates, setting the boolean equal to whether this was successful
            locationRequestsEnabled = createLocationRequest(thisContext,locationProviderClient,mLocationCallback)
        }
    }

    private var state: SensorState = SensorState()

    private val _text = MutableLiveData<String>().apply {
        value = "This is avoid ohio Fragment"
    }
    val text: LiveData<String> = _text

    fun registerSensors() { // use entire block for each sensor you need in this class
        locationProviderClient = LocationServices.getFusedLocationProviderClient(thisContext)
        getLastLocation(thisContext,locationProviderClient,locationUtilCallback)
    }

    fun deregisterSensors() { // set all sensors as inactive
        if(locationRequestsEnabled){
            //stop getting updates
            locationRequestsEnabled = false
            stopLocationUpdates(locationProviderClient,mLocationCallback)
        }
    }

    private fun registerSpecificSensor(sensorType: Int): SensorLiveData { // do not change
        return SensorLiveData(
            thisContext,
            sensorType,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    private class SensorState { // add additional sensors here

    }
}