package com.example.foodhub.Logged.Donee.Detail

import android.util.Log
import androidx.lifecycle.ViewModel

class RequestFormDetailViewModel : ViewModel() {

    init {
        Log.i("RFD VM", "Request Form Detail View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("LoginVM","Request Form Detail View Model has been Destroyed!")
        super.onCleared()
    }

}