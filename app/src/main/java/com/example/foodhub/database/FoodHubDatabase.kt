package com.example.foodhub.database

import android.content.Context
import android.util.Log
import android.view.View
import androidx.room.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.delay
import com.example.foodhub.util.Util
import org.json.JSONArray

@Database(entities = [State::class, Account::class, News::class, Category::class, DonationForm::class, RequestForm::class, AnalysisReport::class, LocationReport::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FoodHubDatabase: RoomDatabase() {

    val util = Util()

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

}