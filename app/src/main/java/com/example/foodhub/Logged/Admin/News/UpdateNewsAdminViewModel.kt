package com.example.foodhub.Logged.Admin.News

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.News
import kotlinx.coroutines.launch

class UpdateNewsAdminViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    lateinit var news: News

    suspend fun getSpecificNewData(context: Context, id: String){
        val db = FoodHubDatabase.getInstance(context)

        var aDF = db.newsDao.get(id)
        news = aDF
    }

}