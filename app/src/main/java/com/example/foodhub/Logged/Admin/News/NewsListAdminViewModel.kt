package com.example.foodhub.Logged.Admin.News

import android.content.Context
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.R
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.News
import kotlinx.coroutines.launch

class NewsListAdminViewModel() : ViewModel() {

    lateinit var news: LiveData<List<News>>
    lateinit var newsSearchList : LiveData<List<News>>

    fun returngetNewsForSearch(): LiveData<List<News>> {
        return news
    }

    fun getNewsData(context: Context){
        val db = FoodHubDatabase.getInstance(context)

        viewModelScope.launch {
            var aDFL = db.newsDao.getAll()
            news = aDFL
            Log.i("getSpecific" , news.toString())
        }
    }

    fun searchNews(context: Context, searchTitle: String){
        val db = FoodHubDatabase.getInstance(context)

        viewModelScope.launch {
            var result = db.newsDao.searchNews("%$searchTitle%")
            newsSearchList = result
        }
    }

}