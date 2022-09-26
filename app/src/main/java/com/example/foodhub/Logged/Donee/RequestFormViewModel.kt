package com.example.foodhub.Logged.Donee

import android.util.Log
import androidx.lifecycle.ViewModel

class RequestFormViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    init {
        Log.i("RequestFormVM", "Request Form View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("RequestFormVM","Request Form View Model has been Destroyed!")
        super.onCleared()
    }
}