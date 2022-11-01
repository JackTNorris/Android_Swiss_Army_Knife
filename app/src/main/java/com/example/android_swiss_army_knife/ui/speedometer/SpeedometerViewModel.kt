package com.example.android_swiss_army_knife.ui.speedometer

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel

class SpeedometerViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is speedometer Fragment"
    }
    val text: LiveData<String> = _text
}