package com.example.foodhub.Logged.Admin.FormManagement.Request

import android.util.Log
import androidx.lifecycle.ViewModel

class AdminRequestFormListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    init {
        Log.i("AdminRequestFormListVM", "Admin Request Form List View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("AdminRequestFormListVM","Admin Request Form List View Model has been Destroyed!")
        super.onCleared()
    }
}