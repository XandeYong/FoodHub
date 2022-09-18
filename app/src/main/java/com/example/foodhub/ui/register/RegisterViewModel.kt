package com.example.foodhub.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    init {
        Log.i("RegisterVM", "Register View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("RegisterVM","Register View Model has been Destroyed!")
        super.onCleared()
    }

}