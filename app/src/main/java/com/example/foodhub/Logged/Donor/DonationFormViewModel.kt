package com.example.foodhub.Logged.Donor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DonationFormViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    lateinit var food: MutableLiveData<String>
    lateinit var test: String

    init {
        Log.i("DonationFormVM", "Donation Form View Model has been Created!")

    }

    override fun onCleared() {
        Log.i("DonationFormVM", "Donation Form View Model has been Destroyed!")
        super.onCleared()
    }

//    fun setValue(paramfood: String){
//        food.value = paramfood
//        //Log.i("DonationFormVM", "$paramfood")
//    }

    fun setValue2(paramtest: String) {
        test = paramtest
        //Log.i("DonationFormVM", "$paramfood")
    }

}