package com.example.foodhub.Logged.Donee

import android.content.Context
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodhub.database.Category
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.RequestForm

class RequestFormViewModel : ViewModel() {
    var latestReqForm: RequestForm = RequestForm()
    var newReqForm: RequestForm = RequestForm()
    lateinit var categoryList: LiveData<List<Category>>
    lateinit var quantity: String

    init {
        Log.i("RequestFormVM", "Request Form View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("RequestFormVM","Request Form View Model has been Destroyed!")
        super.onCleared()
    }

    suspend fun getLatestReqForm(context: Context){
        val db = FoodHubDatabase.getInstance(context)
        latestReqForm = db.requestFormDao.getLatest()
    }

    fun generateNewReqFormID(doneeID: String){
        var newID: String = "RF1"
        if(latestReqForm != null) {
            val value: Int=  latestReqForm.requestFormID.substring(2).toInt() + 1
            newID = "RF" + value.toString()
        }
        newReqForm.requestFormID = newID
        newReqForm.status = "Pending"
        newReqForm.accountID = doneeID
    }

    fun getCategoryList(context: Context){
        val db = FoodHubDatabase.getInstance(context)
        var category = db.categoryDao.getAll()
        categoryList = category
    }

    fun setValueFromEditTextView(qty: String){
        quantity = qty.trim()
    }

    fun validateQuantity(): Boolean {
        var status = quantity.isNotEmpty()

        if (status == true) {
            if (quantity.isDigitsOnly()) {
                if (quantity.toInt() <= 0) {
                    status = false
                } else {
                    status = true
                }
            } else {
                status = false
            }
        }

        if (status == true) {
            newReqForm.quantity = quantity.toInt()
        }
        return status
    }

    fun getSelectedCategoryID(ctg: Category){
        newReqForm.categoryID = ctg.categoryID
    }

    fun insertReqFormToDB(context: Context): Int{
        val db = FoodHubDatabase.getInstance(context)
        var value: Long = 0
        try{
            value = db.requestFormDao.insertReqForm(newReqForm)
        }catch (ex: Exception){
            Log.i("Insert Failed", ex.toString());
        }
        return value.toInt()
    }

}