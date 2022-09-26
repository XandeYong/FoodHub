package com.example.foodhub.Logged.Admin.FormManagement.Donation

import android.util.Log
import androidx.lifecycle.ViewModel

class AdminDonationFormDetailViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    init {
        Log.i("AdminDonationFormDetailVM", "Admin Donation Form Detail View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("AdminDonationFormDetailVM","Admin Donation Form Detail View Model has been Destroyed!")
        super.onCleared()
    }

}