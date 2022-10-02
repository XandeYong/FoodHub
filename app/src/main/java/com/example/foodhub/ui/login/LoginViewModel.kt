package com.example.foodhub.ui.login

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.coroutines.coroutineContext

class LoginViewModel : ViewModel() {

    init {
        Log.i("LoginVM", "Login View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("LoginVM","Login View Model has been Destroyed!")
        super.onCleared()
    }

}