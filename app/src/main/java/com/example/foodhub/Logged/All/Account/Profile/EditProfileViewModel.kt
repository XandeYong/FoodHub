package com.example.foodhub.Logged.All.Account.Profile

import android.util.Log
import androidx.lifecycle.ViewModel

class EditProfileViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    init {
        Log.i("EditProfileVM", "Edit Profile View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("EditProfileVM","Edit Profile View Model has been Destroyed!")
        super.onCleared()
    }
}