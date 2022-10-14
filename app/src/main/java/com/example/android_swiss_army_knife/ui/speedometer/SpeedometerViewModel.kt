package com.example.android_swiss_army_knife.ui.speedometer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SpeedometerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is speedometer Fragment"
    }
    val text: LiveData<String> = _text
}