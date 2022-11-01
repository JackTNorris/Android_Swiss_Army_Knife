package com.example.android_swiss_army_knife.ui.metal_detector

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel

class MetalDetectorViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is metal detector Fragment"
    }
    val text: LiveData<String> = _text
}