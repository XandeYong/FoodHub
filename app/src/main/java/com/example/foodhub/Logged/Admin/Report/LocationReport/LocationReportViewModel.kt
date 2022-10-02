package com.example.foodhub.Logged.Admin.Report.LocationReport

import android.util.Log
import androidx.lifecycle.ViewModel

class LocationReportViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    init {
        Log.i("LocationReportVM", "Location Report View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("LocationReportVM","Location Report View Model has been Destroyed!")
        super.onCleared()
    }

}