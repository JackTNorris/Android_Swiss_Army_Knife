package com.example.android_swiss_army_knife.ui.barometer

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.*
import com.example.android_swiss_army_knife.util.SensorLiveData

/*
For each sensor, make sure to use registerSensors and create each sensor using registerSpecificSensor
registerSpecificSensor should not be modified
Be sure to deregister each sensor as well
Add variable for each sensor in SensorState
In summary, paste everything in this class and modify to use your specific sensor.
 */

class BarometerViewModel(application: Application) : AndroidViewModel(application) {
    private var state: SensorState = SensorState()

    val text = MutableLiveData<String>().apply {
        value = "0.0"
    }

    fun registerSensors() { // use entire block for each sensor you need in this class
        state!!.sensorBarometerLiveData = registerSpecificSensor(Sensor.TYPE_PRESSURE) // for each sensor
        state!!.sensorBarometerLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                text.value = event.value.toString()
            }
        } // for each sensor
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