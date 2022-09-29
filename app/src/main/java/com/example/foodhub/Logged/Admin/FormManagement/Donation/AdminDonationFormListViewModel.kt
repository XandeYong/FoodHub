package com.example.foodhub.Logged.Admin.FormManagement.Donation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.database.DonationForm
import com.example.foodhub.database.FoodHubDatabase
import kotlinx.coroutines.launch

class AdminDonationFormListViewModel : ViewModel() {
    lateinit var adminDFL: LiveData<List<DonationForm>>

    init {
        Log.i("AdminDonationFormListVM", "Admin Donation Form List View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("AdminDonationFormListVM","Admin Donation Form List View Model has been Destroyed!")
        super.onCleared()
    }

    fun getAdminDonationFormList(context: Context){
        val db = FoodHubDatabase.getInstance(context)

        viewModelScope.launch {
            var aDFL = db.donationFormDao.getAll()
            adminDFL = aDFL
        }
    }

    fun searchAdminDonationForm(context: Context, id: String){
        val db = FoodHubDatabase.getInstance(context)

        viewModelScope.launch {
            var search = db.donationFormDao.searchDF(id)
            adminDFL = search
        }
    }

}