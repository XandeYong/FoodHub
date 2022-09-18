package com.example.foodhub.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    init {
        Log.i("LoginVM", "Login View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("LoginVM","Login View Model has been Destroyed!")
        super.onCleared()
    }

}