package com.example.foodhub.Logged.Donor

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodhub.R

class DonationFormListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
//    fab.setOnClickListener {
//        // Respond to FAB click
//    }
    init {
        Log.i("DonationFormListVM", "Donation Form List View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("DonationFormListVM","Donation Form List View Model has been Destroyed!")
        super.onCleared()
    }


}