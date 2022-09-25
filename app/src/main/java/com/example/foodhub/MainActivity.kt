package com.example.foodhub

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.HeaderNavigationDrawerBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private lateinit var profileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch { FoodHubDatabase.getInstance(applicationContext).syncData() }
        navSetup()

        var login = true
        if (login) {
            navDrawerSetup()

            var account = "donor"
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

//            navigationView.setNavigationItemSelectedListener {
//                when(it.itemId) {
//                    R.id.logout ->  {
//                        login = false
//                        navController.navigate(MainFragmentDirections.actionMainFragmentSelf())
//                    }
//                }
//                true
//            }
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
        onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
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

}