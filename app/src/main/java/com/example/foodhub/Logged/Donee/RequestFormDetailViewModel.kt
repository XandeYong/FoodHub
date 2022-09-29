package com.example.foodhub.Logged.Donee

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodhub.database.Category
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.RequestForm

class RequestFormDetailViewModel : ViewModel() {
    lateinit var requestForm: RequestForm
    lateinit var ctg: Category

    init {
        Log.i("RequestFormDetailVM", "Request Form Detail View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("RequestFormDetailVM","Request Form Detail View Model has been Destroyed!")
        super.onCleared()
    }

    suspend fun getReqForm(context: Context, reqFormID: String){
        val db = FoodHubDatabase.getInstance(context)

        var result = db.requestFormDao.get(reqFormID)
        requestForm = result
    }

    suspend fun getCategory(context: Context, categoryID: String){
        val db = FoodHubDatabase.getInstance(context)
        ctg = db.categoryDao.get(categoryID)
    }

    suspend fun updateStatusToDB(context: Context): Int{
        val db = FoodHubDatabase.getInstance(context)

        var result = db.requestFormDao.updateStatus("Deleted", requestForm.requestFormID)

        return result
    }

}