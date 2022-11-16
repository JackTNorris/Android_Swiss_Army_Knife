package com.example.android_swiss_army_knife.ui.metal_detector

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import com.example.android_swiss_army_knife.SensorLiveData
import kotlin.math.sqrt

class MetalDetectorViewModel(application: Application) : AndroidViewModel(application) {
    private var state: MetalDetectorViewModel.SensorState = MetalDetectorViewModel.SensorState()

    private var magneticFieldMeasurementNorth: String = "0.0"
    private var magneticFieldMeasurementEast: String = "0.0"
    private var magneticFieldMeasurementUp: String = "0.0"

    val text = MutableLiveData<String>().apply {
        value = "0.0"
    }
//    val text: LiveData<String> = text

    fun updateTextWithSensorValue() {
       text.value =" MAGNETIC FIELD: " + (sqrt(magneticFieldMeasurementNorth.toDouble() * magneticFieldMeasurementNorth.toDouble()
               + magneticFieldMeasurementEast.toDouble() * magneticFieldMeasurementEast.toDouble()
               + magneticFieldMeasurementUp.toDouble() * magneticFieldMeasurementUp.toDouble()))
    }

    fun registerSensors() { // use entire block for each sensor you need in this class
        state!!.sensorMagneticFieldLiveData = registerSpecificSensor(Sensor.TYPE_MAGNETIC_FIELD) // for each sensor
        state!!.sensorMagneticFieldLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                magneticFieldMeasurementNorth = event.value.toString()
                magneticFieldMeasurementEast = event.value.toString()
                magneticFieldMeasurementUp = event.value.toString()
                updateTextWithSensorValue()
            }
        }
    }


    fun deregisterSensors() { // set all sensors as inactive
        state!!.sensorMagneticFieldLiveData!!.setInactive() // required for each sensor you use
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