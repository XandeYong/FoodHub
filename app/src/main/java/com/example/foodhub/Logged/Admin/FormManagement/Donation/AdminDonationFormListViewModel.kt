package com.example.foodhub.Logged.Admin.FormManagement.Donation

import android.util.Log
import androidx.lifecycle.ViewModel

class AdminDonationFormListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    init {
        Log.i("DonationFormListVM", "Donation Form List View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("DonationFormListVM","Donation Form List View Model has been Destroyed!")
        super.onCleared()
    }
}