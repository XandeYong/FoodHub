package com.example.foodhub.database

import android.content.Context
import android.util.Log
import androidx.room.*
import kotlinx.coroutines.delay

@Database(entities = [State::class, Account::class, News::class, Category::class, DonationForm::class, RequestForm::class, AnalysisReport::class, LocationReport::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FoodHubDatabase: RoomDatabase() {

    abstract val stateDao: StateDao
    abstract val accountDao: AccountDao
    abstract val newsDao: NewsDao
    abstract val categoryDao: CategoryDao
    abstract val donationFormDao: DonationFormDao
    abstract val requestFormDao: RequestFormDao
    abstract val analysisReportDao: AnalysisReportDao
    abstract val locationReportDao: LocationReportDao

    companion object {

        @Volatile
        private var INSTANCE: FoodHubDatabase? = null
        fun getInstance(context: Context): FoodHubDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FoodHubDatabase::class.java,
                        "foodhub_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }

                return instance
            }
        }

    }

    suspend fun syncData() {
        var i = 0
        while (true) {
            Log.i("syncData", "working: " + ++i)
            stateSync()
            newsSync()
            categorySync()
            donationFormSync()
            requestFormSync()
            analysisReportSync()
            locationReportSync()
            delay(300000)
        }
    }

    fun stateSync() {

    }

    fun newsSync() {

    }

    fun categorySync() {

    }

    fun donationFormSync() {

    }

    fun requestFormSync() {

    }

    fun analysisReportSync() {

    }

    fun locationReportSync() {

    }

}