package com.example.foodhub.Logged.Admin.News

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpdateNewsAdminViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val typeSomethings = MutableLiveData<String>()
    fun startTypeSomethings () {
        this.typeSomethings.value = "Can working"
    }

}