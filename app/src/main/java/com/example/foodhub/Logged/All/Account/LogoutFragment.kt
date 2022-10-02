package com.example.foodhub.Logged.All.Account

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodhub.MainActivity
import com.example.foodhub.R
import com.example.foodhub.database.FoodHubDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class LogoutFragment : Fragment() {

    companion object {
        fun newInstance() = LogoutFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = FoodHubDatabase.getInstance(requireContext())
        val sharedPref = requireContext().getSharedPreferences("login_S", AppCompatActivity.MODE_PRIVATE)

        var mainActivity = requireActivity() as MainActivity
        var navigationView = mainActivity.navigationView
        var drawerLayout = mainActivity.drawerLayout
        var actionBar = mainActivity.supportActionBar

        sharedPref.edit().clear().apply()
        lifecycleScope.launch {
            db.accountDao.clear()
            Log.d("logout", sharedPref.getString("accountID", null).toString())

            findNavController().navigate(LogoutFragmentDirections.actionLogoutFragmentToMainFragment())
            //requireActivity().supportFragmentManager.popBackStack()
            //mainActivity.onSupportNavigateUp()


            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            actionBar!!.setDisplayHomeAsUpEnabled(false)
            actionBar!!.setHomeButtonEnabled(false)


            navigationView.menu.setGroupVisible(R.id.account_group, false)
            navigationView.menu.setGroupVisible(R.id.donor_module_group, false)
            navigationView.menu.setGroupVisible(R.id.donee_module_group, false)
            navigationView.menu.setGroupVisible(R.id.admin_module_group, false)
        }

        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

}