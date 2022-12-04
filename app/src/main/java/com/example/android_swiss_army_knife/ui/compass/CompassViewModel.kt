package com.example.android_swiss_army_knife.ui.compass

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import com.example.android_swiss_army_knife.util.SensorLiveData

class CompassViewModel(application: Application) : AndroidViewModel(application) {
    private var state: CompassViewModel.SensorState = CompassViewModel.SensorState()
    private var accelerometerMeasurement: String = "0.0"
    private var magneticFieldMeasurement: String = "0.0"
    val text = MutableLiveData<String>().apply {
        value = "0.0"
    }
    // val text: LiveData<String> = _text

    fun updateTextWithSensorValues() {
        text.value = "ACCELEROMETER: " + accelerometerMeasurement + "\nMAGNETIC FIELD: " + magneticFieldMeasurement
    }
    fun registerSensors() { // use entire block for each sensor you need in this class
        state!!.sensorMagneticFieldLiveData = registerSpecificSensor(Sensor.TYPE_MAGNETIC_FIELD) // for each sensor
        state!!.sensorAccelerometerLiveData = registerSpecificSensor(Sensor.TYPE_ACCELEROMETER)
        state!!.sensorMagneticFieldLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                magneticFieldMeasurement = event.value.toString()
                updateTextWithSensorValues()
            }
        } // for each sensor
        state!!.sensorAccelerometerLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                accelerometerMeasurement = event.value.toString()
                updateTextWithSensorValues()
            }
        } // for each sensor
    }

    fun deregisterSensors() { // set all sensors as inactive
        state!!.sensorMagneticFieldLiveData!!.setInactive() // required for each sensor you use
        state!!.sensorAccelerometerLiveData!!.setInactive()
    }

    private fun registerSpecificSensor(sensorType: Int): SensorLiveData { // do not change
        return SensorLiveData(
            getApplication<Application>().applicationContext,
            sensorType,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    private class SensorState { // add additional sensors here
        var sensorAccelerometerLiveData: SensorLiveData? = null
        var sensorMagneticFieldLiveData: SensorLiveData? = null

    }
}