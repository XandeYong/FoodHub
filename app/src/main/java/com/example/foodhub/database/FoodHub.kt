package com.example.foodhub.database

import android.graphics.Bitmap
import androidx.room.*
import java.util.*

/*
*
* ID:
*   State           = S1
*   Account:
*       Donor       = DO1
*       Donee       = DE1
*       Admin       = A1
*
*   News            = N1
*   Category        = C1
*   DonationForm    = DF1
*   RequestForm     = RF1
*   AnalysisReport  = AR1
*   LocationReport  = LR1
*
* Status:
*   DonationForm    = {Pending, Donated, Deleted}
*   RequestForm     = {Pending, Fulfilled, Deleted}
*
* */

@Entity(tableName = "state_table")
data class State(
    @PrimaryKey var stateID: String = "", //S1
    @ColumnInfo(name = "name") var name: String? = "",
    @ColumnInfo(name = "createdAt") var createdAt: String? = generateDate(),
    @ColumnInfo(name = "updatedAt") var updatedAt: String? = generateDate()
)



@Entity(tableName = "account_table")
data class Account(
    @PrimaryKey var accountID: String = "", //Donor: DO1, Donee: DE1, Admin: A1
    @ColumnInfo(name = "name") var name: String? = "",
    @ColumnInfo(name = "image") var image: Bitmap? = null,
    @ColumnInfo(name = "address") var address: String? = "",
    @ColumnInfo(name = "stateID") var stateID: String? = "",
    @ColumnInfo(name = "dob") var dob: Date? = null,
    @ColumnInfo(name = "gender") var gender: String? = "",
    @ColumnInfo(name = "email") var email: String? = "",
    @ColumnInfo(name = "password") var password: String? = "",
    @ColumnInfo(name = "accountType") var accountType: String? = "",
    @ColumnInfo(name = "createdAt") var createdAt: String? = generateDate(),
    @ColumnInfo(name = "updatedAt") var updatedAt: String? = generateDate()
)



@Entity(tableName = "news_table")
data class News(
    @PrimaryKey var newsID: String = "", //N1
    @ColumnInfo(name = "title") var title: String? = "",
    @ColumnInfo(name = "image") var image: Bitmap? = null,
    @ColumnInfo(name = "url") var url: String? = "",
    @ColumnInfo(name = "createdAt") var createdAt: String? = generateDate(),
    @ColumnInfo(name = "updatedAt") var updatedAt: String? = generateDate()
)



@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey var categoryID: String = "", //C1
    @ColumnInfo(name = "name") var name: String? = "",
    @ColumnInfo(name = "createdAt") var createdAt: String? = generateDate(),
    @ColumnInfo(name = "updatedAt") var updatedAt: String? = generateDate()
){
    override fun toString(): String {
        return name.toString()
    }
}



@Entity(tableName = "donation_form_table")
data class DonationForm(
    @PrimaryKey var donationFormID: String = "", //DF1
    @ColumnInfo(name = "categoryID") var categoryID: String? = "",
    @ColumnInfo(name = "food") var food: String? = "",
    @ColumnInfo(name = "quantity") var quantity: Int? = null,
    @ColumnInfo(name = "status") var status: String? = "",
    @ColumnInfo(name = "accountID") var accountID: String? = "", //Foreign Key
    @ColumnInfo(name = "createdAt") var createdAt: String? = generateDate(),
    @ColumnInfo(name = "updatedAt") var updatedAt: String? = generateDate()
)



@Entity(tableName = "request_form_table")
data class RequestForm(
    @PrimaryKey var requestFormID: String , //RF1
    @ColumnInfo(name = "categoryID") var categoryID: String? = "",
    @ColumnInfo(name = "quantity") var quantity: Int? = null,
    @ColumnInfo(name = "status") var status: String? = "",
    @ColumnInfo(name = "accountID") var accountID: String?, //Foreign Key
    @ColumnInfo(name = "createdAt") var createdAt: String? = generateDate(),
    @ColumnInfo(name = "updatedAt") var updatedAt: String? = generateDate()
)



@Entity(tableName = "analysis_report_table")
data class AnalysisReport(
    @PrimaryKey var analysisReportID: String, //AR1
    @ColumnInfo(name = "totalDonor") var totalDonor: Int? = null,
    @ColumnInfo(name = "totalDonee") var totalDonee: Int? = null,
    @ColumnInfo(name = "totalUser") var totalUser: Int? = null,
    @ColumnInfo(name = "totalDonation") var totalDonation: Int? = null,
    @ColumnInfo(name = "totalRequest") var totalRequest: Int? = null,
    @ColumnInfo(name = "totalNews") var totalNews: Int? = null,
    @ColumnInfo(name = "createdAt") var createdAt: String? = generateDate(),
    @ColumnInfo(name = "updatedAt") var updatedAt: String? = generateDate()
)



@Entity(tableName = "location_report_table")
data class LocationReport(
    @PrimaryKey var locationReportID: String, //LR1
    @ColumnInfo(name = "stateID") var stateID: String? = "",
    @ColumnInfo(name = "totalDonor") var totalDonor: Int? = null,
    @ColumnInfo(name = "totalDonee") var totalDonee: Int? = null,
    @ColumnInfo(name = "createdAt") var createdAt: String? = generateDate(),
    @ColumnInfo(name = "updatedAt") var updatedAt: String? = generateDate()
)