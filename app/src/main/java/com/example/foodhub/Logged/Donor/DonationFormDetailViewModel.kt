package com.example.foodhub.Logged.Donor

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodhub.database.Category
import com.example.foodhub.database.DonationForm
import com.example.foodhub.database.FoodHubDatabase

class DonationFormDetailViewModel : ViewModel() {
    lateinit var donationF: DonationForm
    lateinit var category: Category

    init {
        Log.i("DonationFormDetailVM", "Donation Form Detail View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("DonationFormDetailVM","Donation Form Detail View Model has been Destroyed!")
        super.onCleared()
    }

    suspend fun getDonationForm(context: Context, donationFormID: String){
        val db = FoodHubDatabase.getInstance(context)

        var dF = db.donationFormDao.get(donationFormID)
        donationF = dF
    }

    suspend fun getCategory(context: Context, categoryID: String){
        val db = FoodHubDatabase.getInstance(context)
        category = db.categoryDao.get(categoryID)
    }

    suspend fun updateStatusToDB(context: Context): Int{
        val db = FoodHubDatabase.getInstance(context)

        var value = db.donationFormDao.updateStatus("Deleted", donationF.donationFormID)

        return value
    }


}