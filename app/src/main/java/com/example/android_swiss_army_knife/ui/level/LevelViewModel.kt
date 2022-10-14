package com.example.android_swiss_army_knife.ui.level

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LevelViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is level Fragment"
    }
    val text: LiveData<String> = _text
}