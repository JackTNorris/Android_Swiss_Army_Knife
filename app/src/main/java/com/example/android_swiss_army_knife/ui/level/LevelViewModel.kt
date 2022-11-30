package com.example.android_swiss_army_knife.ui.level

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import com.example.android_swiss_army_knife.SensorLiveData

class LevelViewModel(application: Application) : AndroidViewModel(application) {

    private var state: LevelViewModel.SensorState = LevelViewModel.SensorState()

    val text = MutableLiveData<String>().apply {
        value = "This is level Fragment"
    }

    val x_rotation = MutableLiveData<Float>().apply {
        value = 0.0f
    }
    val y_rotation = MutableLiveData<Float>().apply {
        value = 0.0f
    }
    val z_rotation = MutableLiveData<Float>().apply {
        value = 0.0f
    }

    fun registerSensors() { // use entire block for each sensor you need in this class
        state!!.sensorLevelLiveData = registerSpecificSensor(Sensor.TYPE_ROTATION_VECTOR) // for each sensor
        state!!.sensorLevelLiveData!!.observeForever { event: SensorLiveData.Event? ->

            if (event != null) {
                x_rotation.value = event.values[0]
                y_rotation.value = event.values[1]
                z_rotation.value = event.values[2]
                text.value = event.value.toString()
            }

        } // for each sensor
    }

    private fun registerSpecificSensor(sensorType: Int): SensorLiveData { // do not change
        return SensorLiveData(
            getApplication<Application>().applicationContext,
            sensorType,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    fun deregisterSensors() { // set all sensors as inactive
        state!!.sensorLevelLiveData!!.setInactive() // required for each sensor you use
    }

    private class SensorState { // add additional sensors here
        var sensorLevelLiveData: SensorLiveData? = null // new var for each sensor if multiple are needed
    }
}