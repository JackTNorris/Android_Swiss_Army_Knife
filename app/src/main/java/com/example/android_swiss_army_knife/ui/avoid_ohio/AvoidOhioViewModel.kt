package com.example.android_swiss_army_knife.ui.avoid_ohio

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel

class AvoidOhioViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is avoid ohio Fragment"
    }
    val text: LiveData<String> = _text
}