package com.example.android_swiss_army_knife.ui.compass

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.android_swiss_army_knife.SensorLiveData

class CompassViewModel(application: Application) : AndroidViewModel(application) {
    private var state: CompassViewModel.SensorState = CompassViewModel.SensorState()
    private var accelerometerMeasurement: FloatArray = FloatArray(3)
    private var magneticFieldMeasurement: FloatArray = FloatArray(3)
    private val mR = FloatArray(9)
    private val mOrientation = FloatArray(3)
    private val ALPHA = 0.25f

    val compass_rotation = MutableLiveData<Double>().apply {
        value = 0.0
    }

    fun updateTextWithSensorValues() {
        SensorManager.getRotationMatrix(mR, null, accelerometerMeasurement, magneticFieldMeasurement);
        SensorManager.getOrientation(mR, mOrientation);
        var compassRadianMeasurement = mOrientation[0]
        var compassDegreeMeasurement = Math.toDegrees(compassRadianMeasurement.toDouble())
        if (Math.abs(compassDegreeMeasurement - compass_rotation.value!!) > 3)
        {
            compass_rotation.value = -compassDegreeMeasurement
        }
    }

    // https://talesofcode.com/developing-compass-android-application/
    private fun lowPassFilter(input: FloatArray, output: FloatArray?): FloatArray? {
        if (output == null) return input
        for (i in input.indices) {
            output[i] = output[i] + ALPHA * (input[i] - output[i])
        }
        return output
    }

    fun registerSensors() { // use entire block for each sensor you need in this class
        state!!.sensorMagneticFieldLiveData = registerSpecificSensor(Sensor.TYPE_MAGNETIC_FIELD) // for each sensor
        state!!.sensorAccelerometerLiveData = registerSpecificSensor(Sensor.TYPE_ACCELEROMETER)
        state!!.sensorMagneticFieldLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                lowPassFilter(event.values.clone(), magneticFieldMeasurement)
                updateTextWithSensorValues()
            }
        } // for each sensor
        state!!.sensorAccelerometerLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                lowPassFilter(event.values.clone(), accelerometerMeasurement)
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