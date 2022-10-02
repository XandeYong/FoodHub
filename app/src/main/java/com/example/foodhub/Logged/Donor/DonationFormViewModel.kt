package com.example.foodhub.Logged.Donor

import android.content.Context
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.*
import com.example.foodhub.database.Category
import com.example.foodhub.database.DonationForm
import com.example.foodhub.database.FoodHubDatabase

class DonationFormViewModel : ViewModel() {
    var latestDF: DonationForm = DonationForm()
    var newDonationForm: DonationForm = DonationForm()
    lateinit var categoryList: LiveData<List<Category>>
    lateinit var food: String
    lateinit var qty: String

    init {
        Log.i("DonationFormVM", "Donation Form View Model has been Created!")
    }

    override fun onCleared() {
        Log.i("DonationFormVM", "Donation Form View Model has been Destroyed!")
        super.onCleared()
    }


    suspend fun getLatestDonationForm(context: Context){
        val db = FoodHubDatabase.getInstance(context)

        latestDF = db.donationFormDao.getLatest()
    }

    //add 1 to ID to make new ID
    fun generateNewDonationFormID(){
        var newID: String = "DF1"
        if(latestDF != null) {
            val value: Int=  latestDF.donationFormID.substring(2).toInt() + 1
            newID = "DF" + value.toString()
        }
        newDonationForm.donationFormID = newID
        newDonationForm.status = "Pending"
        newDonationForm.accountID = latestDF.accountID
    }

    fun getCategoryList(context: Context){
        val db = FoodHubDatabase.getInstance(context)

        var category = db.categoryDao.getAll()
        categoryList = category

    }


    fun setValueFromEditTextView(foodValue: String, qtyValue: String){
        food = foodValue.trim()
        qty = qtyValue.trim()
    }

    fun validateFood(): Boolean {
        var status = food.isNotEmpty()

        if(status == true){
            val regex = "^[A-Za-z ]*$".toRegex()
            status = regex.matches(food)
        }

        if (status == true){
            newDonationForm.food = food
        }
        return status
    }

    fun validateQuantity(): Boolean {
        var status = qty.isNotEmpty()

        if(status == true){
            status = qty.isDigitsOnly()
        }

        if (status == true){
            newDonationForm.quantity = qty.toInt()
        }
        return status
    }

    fun getSelectedCategoryID(category: Category){
        newDonationForm.categoryID = category.categoryID
    }

     fun insertDonationFormToDB(context: Context): Int{
        val db = FoodHubDatabase.getInstance(context)
        var value: Long = 0

         try{
            value = db.donationFormDao.insertDonationForm(newDonationForm)

        }catch (ex: Exception){
            Log.i("Insert Failed", ex.toString());
        }

    return value.toInt()
    }


}