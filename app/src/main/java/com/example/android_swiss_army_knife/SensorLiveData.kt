package com.example.android_swiss_army_knife

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData

internal class SensorLiveData(ctxt: Context, sensorType: Int, delay: Int) :
    LiveData<SensorLiveData.Event?>() {
    private val sensorManager: SensorManager
    private val sensor: Sensor?
    private val delay: Int
    override fun onActive() {
        super.onActive()
        sensorManager.registerListener(listener, sensor, delay)
    }

    override fun onInactive() {
        super.onInactive()
        sensorManager.unregisterListener(listener)
    }

    private val listener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            value = Event(event)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // unused
        }
    }

    fun setInactive() { // call this in deregisterSensors
        this.onInactive()
    }

    internal class Event(event: SensorEvent) {
        private val values: FloatArray
        val value: Float

        init {
            values = FloatArray(event.values.size)
            System.arraycopy(event.values, 0, values, 0, event.values.size)
            value = values[0]
        }
    }

    init {
        sensorManager = ctxt.applicationContext
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(sensorType)
        this.delay = delay
        checkNotNull(sensor) { "Cannot obtain the requested sensor" }
    }
}