package com.example.foodhub.Logged.Admin.Report.AnalysisReport

import android.util.Log
import androidx.lifecycle.ViewModel

class AnalysisReportViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    init {
        Log.i("AnalysisReportVM", "Analysis Report View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("AnalysisReportVM","Analysis Report View Model has been Destroyed!")
        super.onCleared()
    }

}