package com.example.android_swiss_army_knife.ui.avoid_ohio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AvoidOhioViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is avoid ohio Fragment"
    }
    val text: LiveData<String> = _text
}