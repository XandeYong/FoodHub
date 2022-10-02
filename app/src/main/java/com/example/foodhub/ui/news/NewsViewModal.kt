package com.example.foodhub.ui.news

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.News
import kotlinx.coroutines.launch


class NewsViewModal() :ViewModel() {

    lateinit var news: LiveData<List<News>>

    fun getNewsData(context: Context){
        val db = FoodHubDatabase.getInstance(context)

        viewModelScope.launch {
            var new = db.newsDao.getAll()
            news = new

        }
    }
}