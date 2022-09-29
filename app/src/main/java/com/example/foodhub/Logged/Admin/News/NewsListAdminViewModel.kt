package com.example.foodhub.Logged.Admin.News

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodhub.R
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.News
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch

class NewsListAdminViewModel() : ViewModel() {

    lateinit var news: LiveData<List<News>>

    fun getNewsData(context: Context){
        val db = FoodHubDatabase.getInstance(context)

        viewModelScope.launch {
            var aDFL = db.newsDao.getAll()
            news = aDFL
        }
    }

}