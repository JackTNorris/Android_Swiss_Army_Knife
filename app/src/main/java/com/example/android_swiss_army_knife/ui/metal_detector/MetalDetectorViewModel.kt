package com.example.android_swiss_army_knife.ui.metal_detector

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import com.example.android_swiss_army_knife.util.SensorLiveData
import kotlin.math.sqrt
import androidx.databinding.adapters.ViewBindingAdapter.setBackground
import androidx.lifecycle.LiveData
import com.example.android_swiss_army_knife.ui.barometer.BarometerViewModel

class MetalDetectorViewModel(application: Application) : AndroidViewModel(application) {
    private var state: MetalDetectorViewModel.SensorState = MetalDetectorViewModel.SensorState()

    private var magneticFieldMeasurementNorth: String = "0.0"
    private var magneticFieldMeasurementEast: String = "0.0"
    private var magneticFieldMeasurementUp: String = "0.0"
    private var magnetism: Double = 0.0

    private val _value = MutableLiveData<Double>().apply {
        value = 0.0
    }

    val value: LiveData<Double>
        get() = _value

    fun updateTextWithSensorValue() {
       magnetism = (sqrt(magneticFieldMeasurementNorth.toDouble() * magneticFieldMeasurementNorth.toDouble()
               + magneticFieldMeasurementEast.toDouble() * magneticFieldMeasurementEast.toDouble()
               + magneticFieldMeasurementUp.toDouble() * magneticFieldMeasurementUp.toDouble()))

       _value.value = ((magnetism*100).toInt().toDouble())/100
    }


    fun registerSensors() { // use entire block for each sensor you need in this class
        state!!.sensorMagneticFieldLiveData = registerSpecificSensor(Sensor.TYPE_MAGNETIC_FIELD) // for each sensor
        state!!.sensorMagneticFieldLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                magneticFieldMeasurementNorth = event.values[0].toString()
                magneticFieldMeasurementEast = event.values[1].toString()
                magneticFieldMeasurementUp = event.values[2].toString()
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
        var sensorMagneticFieldLiveData: SensorLiveData? = null

    }
}