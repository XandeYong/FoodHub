package com.example.foodhub.Logged.Donee

import android.util.Log
import androidx.lifecycle.ViewModel

class RequestFormDetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    init {
        Log.i("RequestFormDetailVM", "Request Form Detail View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("RequestFormDetailVM","Request Form Detail View Model has been Destroyed!")
        super.onCleared()
    }

}