package com.example.foodhub

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.database.*
import com.example.foodhub.util.Util
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    var util = Util()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    var loginCredential: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("login_S", MODE_PRIVATE)
        val accountType =sharedPref.getString("accountType" , null)
        val accountID =sharedPref.getString("accountID" , null)


        loginCredential = true
        if(!accountID.isNullOrEmpty()) {
            loginCredential = true
            Log.i("true1234", accountID)
        }
        val viewGroup = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        val db = FoodHubDatabase.getInstance(applicationContext)

        lifecycleScope.launch { syncData(viewGroup.rootView, applicationContext) }
        navSetup()

        var login = loginCredential
        if (login) {
            navDrawerSetup()

            var account = accountType.toString()
            when(account) {
                "donee" -> {
                    navigationView.menu.setGroupVisible(R.id.admin_module_group, false)
                    navigationView.menu.setGroupVisible(R.id.donor_module_group, false)
                }
                "donor" -> {
                    navigationView.menu.setGroupVisible(R.id.admin_module_group, false)
                    navigationView.menu.setGroupVisible(R.id.donee_module_group, false)
                }
                "admin" -> {
                    navigationView.menu.setGroupVisible(R.id.donor_module_group, false)
                    navigationView.menu.setGroupVisible(R.id.donee_module_group, false)
                }
            }

        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
        onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        handleNestedFragmentsBackStack()
    }

    override fun onSupportNavigateUp(): Boolean {
        return handleNestedFragmentsBackStack()
    }

    private fun handleNestedFragmentsBackStack(): Boolean {
        val navHostChildFragmentManager = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)?.childFragmentManager
        Log.d("backstack-Start", navHostChildFragmentManager?.backStackEntryCount.toString())

        return if (navHostChildFragmentManager?.backStackEntryCount!! > 0) {
            Log.d("backstack-if", navHostChildFragmentManager.backStackEntryCount.toString())
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigateUp() || super.onSupportNavigateUp()
            false
        } else {
            Log.d("backstack-else", navHostChildFragmentManager.backStackEntryCount.toString())
            if (drawerLayout.isOpen) {
                drawerLayout.close()
            } else {
                drawerLayout.open()
            }
            false
        }
    }

    private fun navSetup() {
        navigationView = findViewById(R.id.navigationView)
        navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)

        NavigationUI.setupWithNavController(navigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun navDrawerSetup() {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_nav_drawer,
            R.string.close_nav_drawer
        )
        appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment), drawerLayout)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.inflateMenu(R.menu.navigation_drawer)

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }


    //sync data
    suspend fun syncData(view: View?, context: Context) {
        val url = "http://10.0.2.2/foodhub_server/"

        Log.i("SyncData", "working: ")
        stateSync(view, context, url)
        newsSync(view, context, url)
        categorySync(view, context, url)
        donationFormSync(view, context, url)
        requestFormSync(view, context, url)
        analysisReportSync(view, context, url)
        locationReportSync(view, context, url)
        delay(30000)
    }

    private suspend fun stateSync(view: View?, context: Context, _url: String) {

        val url = _url + "state.php?request=getAll"
        val list: MutableList<State> = mutableListOf()

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received")
                val jsonArray = response.getJSONArray("data")

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
                lifecycleScope.launch {
                    db.stateDao.syncWithServer(list)
                }

            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(view?.context)
        requestQueue.add(request)

    }

    private suspend fun newsSync(view: View?, context: Context, _url: String) {

        val url = _url + "news.php?request=getAll"
        val list: MutableList<News> = mutableListOf()

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received")
                val jsonArray = response.getJSONArray("data")

                lifecycleScope.launch {
                    for (i in 0 until jsonArray.length()) {
                        val jsonObj = jsonArray.getJSONObject(i)
                        val id = jsonObj.getString("news_id")
                        val title = jsonObj.getString("title")
                        val imageJson = jsonObj.getString("image")
                        val newsUrl = jsonObj.getString("url")
                        val createdAt = jsonObj.getString("created_at")
                        val updatedAt = jsonObj.getString("updated_at")

                        val image:Bitmap = util.getBitmap(imageJson, applicationContext)

                        val news: News = News(id, title, image, newsUrl, createdAt, updatedAt)
                        list.add(news)
                    }

                    val db = FoodHubDatabase.getInstance(context)
                    db.newsDao.syncWithServer(list)

                }

            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(view?.context)
        requestQueue.add(request)

    }

    private suspend fun categorySync(view: View?, context: Context, _url: String) {

        val url = _url + "category.php?request=getAll"
        val list: MutableList<Category> = mutableListOf()

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received")
                val jsonArray = response.getJSONArray("data")

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
                lifecycleScope.launch {
                    db.categoryDao.syncWithServer(list)
                }

            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(view?.context)
        requestQueue.add(request)

    }

    private suspend fun donationFormSync(view: View?, context: Context, _url: String) {

        val url = _url + "donation_form.php?request=getAll"
        val list: MutableList<DonationForm> = mutableListOf()

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received")
                val jsonArray = response.getJSONArray("data")

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
                lifecycleScope.launch {
                    db.donationFormDao.syncWithServer(list)
                }

            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(view?.context)
        requestQueue.add(request)

    }

    private suspend fun requestFormSync(view: View?, context: Context, _url: String) {

        val url = _url + "request_form.php?request=getAll"
        val list: MutableList<RequestForm> = mutableListOf()

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received")
                val jsonArray = response.getJSONArray("data")

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
                lifecycleScope.launch {
                    db.requestFormDao.syncWithServer(list)
                }

            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(view?.context)
        requestQueue.add(request)

    }

    private suspend fun analysisReportSync(view: View?, context: Context, _url: String) {

        val url = _url + "analysis_report.php?request=getAll"
        val list: MutableList<AnalysisReport> = mutableListOf()

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received")
                val jsonArray = response.getJSONArray("data")

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
                lifecycleScope.launch {
                    db.analysisReportDao.syncWithServer(list)
                }

            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(view?.context)
        requestQueue.add(request)

    }

    private suspend fun locationReportSync(view: View?, context: Context, _url: String) {

        val url = _url + "location_report.php?request=getAll"
        val list: MutableList<LocationReport> = mutableListOf()

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received 7")
                val jsonArray = response.getJSONArray("data")

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
                lifecycleScope.launch {
                    db.locationReportDao.syncWithServer(list)
                }

            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(view?.context)
        requestQueue.add(request)

    }

}