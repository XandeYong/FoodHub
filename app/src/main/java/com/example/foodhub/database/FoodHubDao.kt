package com.example.foodhub.database

import androidx.lifecycle.LiveData
import androidx.room.*

interface BaseDao<T> {

    @Insert
    suspend fun insert(obj: T)

    @Insert
    suspend fun insert(obj: MutableList<T>)

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)

}



@Dao
abstract class AccountDao: BaseDao<Account> {

    @Update
    abstract suspend fun updateAt(account: Account)

    @Query("SELECT * FROM account_table WHERE accountID = :id")
    abstract suspend fun get(id: String): Account

    @Query("SELECT * FROM account_table ORDER BY createdAt DESC")
    abstract fun getAll():LiveData<List<Account>>

    @Query("SELECT * FROM account_table ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getLatest(): Account

    @Query("DELETE FROM account_table WHERE accountID = :id")
    abstract suspend fun deleteAt(id: String)

    @Query("DELETE FROM account_table")
    abstract suspend fun clear()

}



@Dao
abstract class NewsDao: BaseDao<News> {

    @Transaction
    open suspend fun syncWithServer(news: MutableList<News>) {
        clear()
        insert(news)
    }

    @Query("SELECT * FROM news_table WHERE newsID = :id")
    abstract suspend fun get(id: String): News

    @Query("SELECT * FROM news_table ORDER BY createdAt DESC")
    abstract fun getAll():LiveData<List<News>>

    @Query("SELECT * FROM news_table ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getLatest(): News

    @Query("DELETE FROM news_table")
    abstract suspend fun clear()

}



@Dao
abstract class CategoryDao: BaseDao<Category> {

    @Transaction
    open suspend fun syncWithServer(category: MutableList<Category>) {
        clear()
        insert(category)
    }

    @Query("SELECT * FROM category_table WHERE categoryID = :id")
    abstract suspend fun get(id: String): Category

    @Query("SELECT * FROM category_table ORDER BY createdAt DESC")
    abstract fun getAll():LiveData<List<Category>>

    @Query("SELECT * FROM category_table ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getLatest(): Category

    @Query("DELETE FROM category_table")
    abstract suspend fun clear()

//add
    @Query("SELECT name FROM category_table ORDER BY createdAt DESC")
    abstract suspend fun getAllCategoryList(): List<String>

}



@Dao
abstract class DonationFormDao: BaseDao<DonationForm> {

    @Transaction
    open suspend fun syncWithServer(donationForm: MutableList<DonationForm>) {
        clear()
        insert(donationForm)
    }

    @Query("SELECT * FROM donation_form_table WHERE donationFormID = :id")
    abstract suspend fun get(id: String): DonationForm

    @Query("SELECT * FROM donation_form_table ORDER BY createdAt DESC")
    abstract fun getAll():LiveData<List<DonationForm>>

    @Query("SELECT * FROM donation_form_table WHERE status != 'Deleted' ORDER BY createdAt DESC")
    abstract fun getAllAvailable():LiveData<List<DonationForm>>

    @Query("SELECT * FROM donation_form_table ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getLatest(): DonationForm

    @Query("DELETE FROM donation_form_table")
    abstract suspend fun clear()

//add
    @Query(" UPDATE donation_form_table SET status=:status WHERE donationFormID = :id")
    abstract suspend fun updateStatus(status: String, id: String): Int

    @Query("SELECT * FROM donation_form_table WHERE accountID = :id AND status != :status ORDER BY createdAt DESC")
    abstract fun getAllListByDonorID(id: String, status: String = "Deleted"): LiveData<List<DonationForm>>

    @Insert
    abstract fun insertDonationForm(donationForm: DonationForm): Long

    @Query("SELECT * FROM donation_form_table WHERE donationFormID LIKE :id ORDER BY createdAt DESC")
    abstract fun searchDF(id: String): LiveData<List<DonationForm>>

    @Query("SELECT * FROM donation_form_table WHERE accountID = :accountID AND donationFormID LIKE :dfID AND status != :status ORDER BY createdAt DESC")
    abstract fun searchDFAvailable(accountID: String, dfID: String, status: String = "Deleted"): LiveData<List<DonationForm>>

}



@Dao
abstract class RequestFormDao: BaseDao<RequestForm> {

    @Transaction
    open suspend fun syncWithServer(requestForm: MutableList<RequestForm>) {
        clear()
        insert(requestForm)
    }

    @Query("SELECT * FROM request_form_table WHERE requestFormID = :id")
    abstract suspend fun get(id: String): RequestForm

    @Query("SELECT * FROM request_form_table ORDER BY createdAt DESC")
    abstract fun getAll():LiveData<List<RequestForm>>

    @Query("SELECT * FROM request_form_table ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getLatest(): RequestForm

    @Query("DELETE FROM request_form_table")
    abstract suspend fun clear()

//add
    @Query(" UPDATE request_form_table SET status=:status WHERE requestFormID = :id")
    abstract suspend fun updateStatus(status: String, id: String): Int

    @Query("SELECT * FROM request_form_table WHERE requestFormID LIKE :id ORDER BY createdAt DESC")
    abstract fun searchRF(id: String): LiveData<List<RequestForm>>

    @Query("SELECT * FROM request_form_table WHERE accountID = :id AND status != :status ORDER BY createdAt DESC")
    abstract fun getAllListByDoneeID(id: String, status: String = "Deleted"):LiveData<List<RequestForm>>

    @Query("SELECT * FROM request_form_table WHERE accountID = :accountID AND requestFormID LIKE :rfID AND status != :status ORDER BY createdAt DESC")
    abstract fun searchRFAvl(accountID: String, rfID: String, status: String = "Deleted"): LiveData<List<RequestForm>>

    @Insert
    abstract fun insertReqForm(reqForm: RequestForm): Long

}



@Dao
abstract class AnalysisReportDao: BaseDao<AnalysisReport> {

    @Transaction
    open suspend fun syncWithServer(analysisReport: MutableList<AnalysisReport>) {
        clear()
        insert(analysisReport)
    }

    @Query("SELECT * FROM analysis_report_table WHERE analysisReportID = :id")
    abstract suspend fun get(id: String): AnalysisReport

    @Query("SELECT * FROM analysis_report_table ORDER BY createdAt DESC")
    abstract fun getAll():LiveData<List<AnalysisReport>>

    @Query("SELECT * FROM analysis_report_table ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getLatest(): AnalysisReport

    @Query("DELETE FROM analysis_report_table")
    abstract suspend fun clear()

}



@Dao
abstract class LocationReportDao: BaseDao<LocationReport> {

    @Transaction
    open suspend fun syncWithServer(locationReport: MutableList<LocationReport>) {
        clear()
        insert(locationReport)
    }

    @Query("SELECT * FROM location_report_table WHERE locationReportID = :id")
    abstract suspend fun get(id: String): LocationReport

    @Query("SELECT * FROM location_report_table ORDER BY createdAt DESC")
    abstract fun getAll():LiveData<List<LocationReport>>

    @Query("SELECT * FROM location_report_table ORDER BY createdAt DESC LIMIT 1")
    abstract suspend fun getLatest(): LocationReport

    @Query("DELETE FROM location_report_table")
    abstract suspend fun clear()

}