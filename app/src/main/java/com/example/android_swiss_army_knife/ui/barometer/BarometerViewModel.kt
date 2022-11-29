package com.example.android_swiss_army_knife.ui.barometer

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.*
import com.example.android_swiss_army_knife.SensorLiveData

class BarometerViewModel(application: Application) : AndroidViewModel(application) {
    private var state: SensorState = SensorState()


    private val _text = MutableLiveData<String>().apply {
        value = "0.0"
    }

    val text: LiveData<String>
        get() = _text

    private var units = hpaUnit
    fun updateUnits(pressureUnits: String) {
        //TODO store in var to use before setting value (and add units to end)
        units = pressureUnits
    }

    fun registerSensors() { // use entire block for each sensor you need in this class
        state!!.sensorBarometerLiveData = registerSpecificSensor(Sensor.TYPE_PRESSURE) // for each sensor
        state!!.sensorBarometerLiveData!!.observeForever { event: SensorLiveData.Event? ->
            if (event != null) {
                val tmpSensorValue = event.value.toDouble()
                _text.value = when (units) {
                    atmUnit -> "${tmpSensorValue * 0.000987} $atmUnit"
                    psiUnit -> "${tmpSensorValue * 0.0145} $psiUnit"
                    hpaUnit -> "$tmpSensorValue $hpaUnit"
                    else -> "Bad units given, $units"
                }
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

    companion object {
        const val hpaUnit = "hPa"
        const val psiUnit = "psi"
        const val atmUnit = "atm"
    }
}