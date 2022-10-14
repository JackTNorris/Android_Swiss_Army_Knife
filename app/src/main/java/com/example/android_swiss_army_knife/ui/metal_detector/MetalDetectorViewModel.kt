package com.example.android_swiss_army_knife.ui.metal_detector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MetalDetectorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is metal detector Fragment"
    }
    val text: LiveData<String> = _text
}