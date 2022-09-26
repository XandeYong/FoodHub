package com.example.foodhub.database

import android.content.Context
import android.util.Log
import android.view.View
import androidx.room.*
import kotlinx.coroutines.delay
import com.example.foodhub.util.Util

@Database(entities = [State::class, Account::class, News::class, Category::class, DonationForm::class, RequestForm::class, AnalysisReport::class, LocationReport::class], version = 2, exportSchema = false)
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

    suspend fun syncData(view: View?, context: Context) {
        var i = 0
        val url = "http://10.0.2.2/foodhub_server/"
        while (true) {
            Log.i("SyncData", "working: " + ++i)
            stateSync(view, context, url)
            newsSync(view, context, url)
            categorySync(view, context, url)
            donationFormSync(view, context, url)
            requestFormSync(view, context, url)
            analysisReportSync(view, context, url)
            locationReportSync(view, context, url)
            delay(30000)
        }
    }

    private suspend fun stateSync(view: View?, context: Context, _url: String) {

        val url = _url + "state.php?request=getAll"
        val jsonArray = util.jsonDecodeGet(view, url)
        val list: MutableList<State> = mutableListOf()

        if (jsonArray!!.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                val jsonObj = jsonArray.getJSONObject(i)
                val id = jsonObj.getString("state_id")
                val name = jsonObj.getString("name")
                val createdAt = jsonObj.getString("created_at")
                val updatedAt = jsonObj.getString("updated_at")

                val state: State = State(id, name, createdAt, updatedAt)
                list.add(state)
            }

            val db = FoodHubDatabase.getInstance(context)
            db.stateDao.syncWithServer(list)
        }
    }

    private suspend fun newsSync(view: View?, context: Context, _url: String) {

        val url = _url + "news.php?request=getAll"
        val jsonArray = util.jsonDecodeGet(view, url)
        val list: MutableList<News> = mutableListOf()

        if (jsonArray!!.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                val jsonObj = jsonArray.getJSONObject(i)
                val id = jsonObj.getString("news_id")
                val title = jsonObj.getString("title")
                val image = jsonObj.getString("image")
                val newsUrl = jsonObj.getString("url")
                val createdAt = jsonObj.getString("created_at")
                val updatedAt = jsonObj.getString("updated_at")

                val news: News = News(id, title, util.getBitmap(image, context), newsUrl, createdAt, updatedAt)
                list.add(news)
            }

            val db = FoodHubDatabase.getInstance(context)
            db.newsDao.syncWithServer(list)
        }
    }

    private suspend fun categorySync(view: View?, context: Context, _url: String) {

        val url = _url + "category.php?request=getAll"
        val jsonArray = util.jsonDecodeGet(view, url)
        val list: MutableList<Category> = mutableListOf()

        if (jsonArray!!.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                val jsonObj = jsonArray.getJSONObject(i)
                val id = jsonObj.getString("category_id")
                val name = jsonObj.getString("name")
                val createdAt = jsonObj.getString("created_at")
                val updatedAt = jsonObj.getString("updated_at")

                val category: Category = Category(id, name, createdAt, updatedAt)
                list.add(category)
            }

            val db = FoodHubDatabase.getInstance(context)
            db.categoryDao.syncWithServer(list)
        }
    }

    private suspend fun donationFormSync(view: View?, context: Context, _url: String) {

        val url = _url + "donation_form.php?request=getAll"
        val jsonArray = util.jsonDecodeGet(view, url)
        val list: MutableList<DonationForm> = mutableListOf()

        if (jsonArray!!.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                val jsonObj = jsonArray.getJSONObject(i)
                val id = jsonObj.getString("donation_form_id")
                val categoryID = jsonObj.getString("category_id")
                val food = jsonObj.getString("food")
                val quantity = jsonObj.getInt("quantity")
                val status = jsonObj.getString("status")
                val accountID = jsonObj.getString("account_id")
                val createdAt = jsonObj.getString("created_at")
                val updatedAt = jsonObj.getString("updated_at")

                val donationForm: DonationForm = DonationForm(id, categoryID, food, quantity, status, accountID, createdAt, updatedAt)
                list.add(donationForm)
            }

            val db = FoodHubDatabase.getInstance(context)
            db.donationFormDao.syncWithServer(list)
        }
    }

    private suspend fun requestFormSync(view: View?, context: Context, _url: String) {

        val url = _url + "request_form.php?request=getAll"
        val jsonArray = util.jsonDecodeGet(view, url)
        val list: MutableList<RequestForm> = mutableListOf()

        if (jsonArray!!.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                val jsonObj = jsonArray.getJSONObject(i)
                val id = jsonObj.getString("request_form_id")
                val categoryID = jsonObj.getString("category_id")
                val quantity = jsonObj.getInt("quantity")
                val status = jsonObj.getString("status")
                val accountID = jsonObj.getString("account_id")
                val createdAt = jsonObj.getString("created_at")
                val updatedAt = jsonObj.getString("updated_at")

                val requestForm: RequestForm = RequestForm(id, categoryID, quantity, status, accountID, createdAt, updatedAt)
                list.add(requestForm)
            }

            val db = FoodHubDatabase.getInstance(context)
            db.requestFormDao.syncWithServer(list)
        }
    }

    private suspend fun analysisReportSync(view: View?, context: Context, _url: String) {

        val url = _url + "analysis_report.php?request=getAll"
        val jsonArray = util.jsonDecodeGet(view, url)
        val list: MutableList<AnalysisReport> = mutableListOf()

        if (jsonArray!!.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                val jsonObj = jsonArray.getJSONObject(i)
                val id = jsonObj.getString("analysis_report_id")
                val totalDonor = jsonObj.getInt("total_donor")
                val totalDonee = jsonObj.getInt("total_donee")
                val totalUser = jsonObj.getInt("total_user")
                val totalDonation = jsonObj.getInt("total_donation")
                val totalRequest = jsonObj.getInt("total_request")
                val totalNews = jsonObj.getInt("total_news")
                val createdAt = jsonObj.getString("created_at")
                val updatedAt = jsonObj.getString("updated_at")

                val analysisReport: AnalysisReport = AnalysisReport(id, totalDonor, totalDonee, totalUser, totalDonation, totalRequest, totalNews, createdAt, updatedAt)
                list.add(analysisReport)
            }

            val db = FoodHubDatabase.getInstance(context)
            db.analysisReportDao.syncWithServer(list)
        }
    }

    private suspend fun locationReportSync(view: View?, context: Context, _url: String) {

        val url = _url + "analysis_report.php?request=getAll"
        val jsonArray = util.jsonDecodeGet(view, url)
        val list: MutableList<LocationReport> = mutableListOf()

        if (jsonArray!!.length() > 0) {
            for (i in 0 until jsonArray.length()) {
                val jsonObj = jsonArray.getJSONObject(i)
                val id = jsonObj.getString("location_report_id")
                val stateID = jsonObj.getString("state_id")
                val totalDonor = jsonObj.getInt("total_donor")
                val totalDonee = jsonObj.getInt("total_donee")
                val createdAt = jsonObj.getString("created_at")
                val updatedAt = jsonObj.getString("updated_at")

                val locationReport: LocationReport = LocationReport(id, stateID, totalDonor, totalDonee, createdAt, updatedAt)
                list.add(locationReport)
            }

            val db = FoodHubDatabase.getInstance(context)
            db.locationReportDao.syncWithServer(list)
        }
    }

}