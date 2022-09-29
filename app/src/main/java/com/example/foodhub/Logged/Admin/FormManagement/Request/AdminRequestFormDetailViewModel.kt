package com.example.foodhub.Logged.Admin.FormManagement.Request

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodhub.database.Category
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.RequestForm

class AdminRequestFormDetailViewModel : ViewModel() {
    lateinit var adminRF: RequestForm
    lateinit var category: Category

    init {
        Log.i("AdminRequestFormDetailVM", "Admin Request Form Detail View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("AdminRequestFormDetailVM","Admin Request Form Detail View Model has been Destroyed!")
        super.onCleared()
    }

    suspend fun getAdminRequestForm(context: Context, requestFormID: String){
        val db = FoodHubDatabase.getInstance(context)

        var aRF = db.requestFormDao.get(requestFormID)
        adminRF = aRF
    }

    suspend fun getCategory(context: Context, categoryID: String){
        val db = FoodHubDatabase.getInstance(context)
        category = db.categoryDao.get(categoryID)
    }

    fun checkStatusSelectedPosition(statusListRF: Array<String>): Int{
        var position: Int = 0
        for(status in statusListRF) {
            if(!status.equals(adminRF.status)) {
                position++
            }else{
                break
            }
        }
        return position
    }

    suspend fun updateStatusToDB(context: Context, status: String): Int{
        val db = FoodHubDatabase.getInstance(context)

        var value = db.requestFormDao.updateStatus(status, adminRF.requestFormID)
        if(value == 1){
            adminRF.status = status
        }
        return value
    }

}