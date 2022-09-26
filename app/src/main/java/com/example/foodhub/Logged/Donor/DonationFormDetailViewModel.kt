package com.example.foodhub.Logged.Donor

import android.util.Log
import androidx.lifecycle.ViewModel

class DonationFormDetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    init {
        Log.i("DonationFormDetailVM", "Donation Form Detail View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("DonationFormDetailVM","Donation Form Detail View Model has been Destroyed!")
        super.onCleared()
    }

}