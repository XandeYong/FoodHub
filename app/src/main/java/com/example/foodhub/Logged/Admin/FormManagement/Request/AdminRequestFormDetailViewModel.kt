package com.example.foodhub.Logged.Admin.FormManagement.Request

import android.util.Log
import androidx.lifecycle.ViewModel

class AdminRequestFormDetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    init {
        Log.i("AdminRequestFormDetailVM", "Admin Request Form Detail View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("AdminRequestFormDetailVM","Admin Request Form Detail View Model has been Destroyed!")
        super.onCleared()
    }

}