package com.example.foodhub.Logged.Admin.Report

import android.util.Log
import androidx.lifecycle.ViewModel

class ReportViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    init {
        Log.i("ReportVM", "Report View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("ReportVM","Report View Model has been Destroyed!")
        super.onCleared()
    }
}