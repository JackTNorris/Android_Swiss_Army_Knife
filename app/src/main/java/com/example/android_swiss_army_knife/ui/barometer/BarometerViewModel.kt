package com.example.android_swiss_army_knife.ui.barometer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BarometerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is barometer Fragment"
    }
    val text: LiveData<String> = _text
}