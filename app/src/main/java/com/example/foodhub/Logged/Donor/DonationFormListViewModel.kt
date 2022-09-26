package com.example.foodhub.Logged.Donor

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodhub.R
import com.example.foodhub.database.DonationForm
import com.example.foodhub.database.FoodHubDatabase

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

    suspend fun getData(context: Context): DonationForm {
        val db = FoodHubDatabase.getInstance(context)
        var df = db.donationFormDao.getLatest()
        if (df == null) {
            Log.i("DF", "is null")
            df = DonationForm()
        }

        return df
    }


}