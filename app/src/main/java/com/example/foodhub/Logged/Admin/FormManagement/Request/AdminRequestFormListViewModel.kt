package com.example.foodhub.Logged.Admin.FormManagement.Request

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.RequestForm
import kotlinx.coroutines.launch

class AdminRequestFormListViewModel : ViewModel() {
    lateinit var adminRFL: LiveData<List<RequestForm>>

    init {
        Log.i("AdminRequestFormListVM", "Admin Request Form List View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("AdminRequestFormListVM","Admin Request Form List View Model has been Destroyed!")
        super.onCleared()
    }

    fun getAdminRequestFormList(context: Context){
        val db = FoodHubDatabase.getInstance(context)

        viewModelScope.launch {
            var aRFL = db.requestFormDao.getAll()
            adminRFL = aRFL
        }
    }

    fun searchAdminRequestForm(context: Context, id: String){
        val db = FoodHubDatabase.getInstance(context)

        viewModelScope.launch {
            var search = db.requestFormDao.searchRF("%$id%")
            adminRFL = search
        }
    }

}