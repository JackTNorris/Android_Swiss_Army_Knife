package com.example.android_swiss_army_knife.ui.light

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android_swiss_army_knife.util.SensorLiveData
import androidx.lifecycle.*

class LightViewModel(application: Application) : AndroidViewModel(application) {
    private var state: SensorState = SensorState()

    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    val text: LiveData<String>
        get() = _text

    fun registerSensors() { // use entire block for each sensor you need in this class
        state!!.sensorBarometerLiveData = registerSpecificSensor(Sensor.TYPE_LIGHT) // for each sensor
        state!!.sensorBarometerLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                _text.value = event.value.toString()
            }
        }
    }

    fun deregisterSensors() { // set all sensors as inactive
        state!!.sensorBarometerLiveData!!.setInactive() // required for each sensor you use
    }


    private fun registerSpecificSensor(sensorType: Int): SensorLiveData { // do not change
        return SensorLiveData(
            getApplication<Application>().applicationContext,
            sensorType,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    private class SensorState { // add additional sensors here
        var sensorBarometerLiveData: SensorLiveData? = null // new var for each sensor if multiple are needed
    }
}