package com.example.android_swiss_army_knife.ui.compass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompassViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is compass Fragment"
    }
    val text: LiveData<String> = _text
}