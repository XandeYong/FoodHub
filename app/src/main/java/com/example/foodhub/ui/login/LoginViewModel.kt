package com.example.foodhub.ui.login

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.coroutines.coroutineContext

class LoginViewModel : ViewModel() {

    private val _response = MutableLiveData<Boolean>()
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()


    // The external immutable LiveData for the response String
    val response: LiveData<Boolean>
        get() = _response

    val email: LiveData<String>
        get() = _email

    val password: LiveData<String>
        get() = _password

    init {
        Log.i("LoginVM", "Login View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("LoginVM","Login View Model has been Destroyed!")
        super.onCleared()
    }

}