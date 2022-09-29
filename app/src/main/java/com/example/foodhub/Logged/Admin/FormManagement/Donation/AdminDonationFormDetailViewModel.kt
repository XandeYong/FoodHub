package com.example.foodhub.Logged.Admin.FormManagement.Donation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodhub.database.Category
import com.example.foodhub.database.DonationForm
import com.example.foodhub.database.FoodHubDatabase


class AdminDonationFormDetailViewModel : ViewModel() {
    lateinit var adminDF: DonationForm
    lateinit var category: Category

    init {
        Log.i("AdminDonationFormDetailVM", "Admin Donation Form Detail View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("AdminDonationFormDetailVM","Admin Donation Form Detail View Model has been Destroyed!")
        super.onCleared()
    }

    suspend fun getAdminDonationForm(context: Context, donationFormID: String){
        val db = FoodHubDatabase.getInstance(context)

            var aDF = db.donationFormDao.get(donationFormID)
            adminDF = aDF
    }

    suspend fun getCategory(context: Context, categoryID: String){
        val db = FoodHubDatabase.getInstance(context)
            category = db.categoryDao.get(categoryID)
    }

    fun checkStatusSelectedPosition(statusListDF: Array<String>): Int{
        var position: Int = 0
        for(status in statusListDF) {
            if(!status.equals(adminDF.status)) {
                position++
            }else{
                break
            }
        }
        return position
    }

    suspend fun updateStatusToDB(context: Context, status: String): Int{
        val db = FoodHubDatabase.getInstance(context)

        var value = db.donationFormDao.updateStatus(status, adminDF.donationFormID)
        if(value == 1){
            adminDF.status = status
        }
        return value
    }

}