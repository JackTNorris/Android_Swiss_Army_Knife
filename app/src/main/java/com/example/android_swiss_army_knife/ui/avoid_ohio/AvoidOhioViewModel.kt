package com.example.android_swiss_army_knife.ui.avoid_ohio

import android.Manifest
import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import com.example.android_swiss_army_knife.util.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices

class AvoidOhioViewModel(application: Application) : AndroidViewModel(application) {
    private val thisContext by lazy { getApplication<Application>().applicationContext }
    private var state: SensorState = SensorState()

    // begin location variables
    var locationPermissionEnabled:Boolean = false
    private var locationRequestsEnabled:Boolean = false
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationCallback: LocationCallback

    lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>

    private val locationUtilCallback = object:LocationUtilCallback{
        override fun requestPermissionCallback() {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }
        override fun locationUpdatedCallback(location: Location) {
            state.sensorGpsLiveData = location
            _text.value = "Location is [Lat: ${location.latitude}, Long: ${location.longitude}]\nDirection is: ${compassRotation.value}"
        }
    }

    fun startLocationRequests(){
        if(!locationRequestsEnabled){
            mLocationCallback = createLocationCallback(locationUtilCallback)
            locationRequestsEnabled = createLocationRequest(thisContext,locationProviderClient,mLocationCallback)
        }
    }
    // end location variables

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String> = _text

    private var accelerometerMeasurement: FloatArray = FloatArray(3)
    private var magneticFieldMeasurement: FloatArray = FloatArray(3)
    private val mR = FloatArray(9)
    private val mOrientation = FloatArray(3)

    private val compassRotation = MutableLiveData<Double>().apply {
        value = 0.0
    }

    fun updateTextWithSensorValues() {
        SensorManager.getRotationMatrix(mR, null, accelerometerMeasurement, magneticFieldMeasurement);
        SensorManager.getOrientation(mR, mOrientation);
        var compassRadianMeasurement = mOrientation[0]
        var compassDegreeMeasurement = (Math.toDegrees(compassRadianMeasurement.toDouble())+360)%360;
        if (Math.abs(compassDegreeMeasurement - compassRotation.value!!) > 5)
        {
            compassRotation.value = compassDegreeMeasurement
        }
    }

    fun registerSensors() { // use entire block for each sensor you need in this class
        locationProviderClient = LocationServices.getFusedLocationProviderClient(thisContext)
        getLastLocation(thisContext,locationProviderClient,locationUtilCallback)
        state!!.sensorMagneticFieldLiveData = registerSpecificSensor(Sensor.TYPE_MAGNETIC_FIELD) // for each sensor
        state!!.sensorAccelerometerLiveData = registerSpecificSensor(Sensor.TYPE_ACCELEROMETER)
        state!!.sensorMagneticFieldLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                magneticFieldMeasurement = event.values
                updateTextWithSensorValues()
            }
        } // for each sensor
        state!!.sensorAccelerometerLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                accelerometerMeasurement = event.values
                updateTextWithSensorValues()
            }
        } // for each sensor
    }

    fun deregisterSensors() { // set all sensors as inactive
        if(locationRequestsEnabled){
            //stop getting updates
            locationRequestsEnabled = false
            stopLocationUpdates(locationProviderClient,mLocationCallback)
        }
        state!!.sensorMagneticFieldLiveData!!.setInactive() // required for each sensor you use
        state!!.sensorAccelerometerLiveData!!.setInactive()
    }

    private fun registerSpecificSensor(sensorType: Int): SensorLiveData { // do not change
        return SensorLiveData(
            thisContext,
            sensorType,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    private class SensorState { // add additional sensors here
        lateinit var sensorGpsLiveData: Location
        var sensorAccelerometerLiveData: SensorLiveData? = null
        var sensorMagneticFieldLiveData: SensorLiveData? = null
    }
}