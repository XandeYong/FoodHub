package com.example.foodhub.Logged.Donor

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.database.DonationForm
import com.example.foodhub.database.FoodHubDatabase
import kotlinx.coroutines.launch

class DonationFormListViewModel : ViewModel() {
    lateinit var donationFL: LiveData<List<DonationForm>>

    init {
        Log.i("DonationFormListVM", "Donation Form List View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("DonationFormListVM","Donation Form List View Model has been Destroyed!")
        super.onCleared()
    }

    fun getDonationFormList(context: Context, donorID: String){
        val db = FoodHubDatabase.getInstance(context)

        viewModelScope.launch {
            var dFL = db.donationFormDao.getAllListByDonorID(donorID)
            donationFL = dFL
        }
    }


}