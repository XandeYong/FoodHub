package com.example.foodhub.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foodhub.MainActivity
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentMainBinding
import com.example.foodhub.ui.login.LoginFragment
import com.example.foodhub.ui.news.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding

    private val newsFragment = NewsFragment()
    private val loginFragment = LoginFragment()
    var loginCredential = false

    private lateinit var sharedPref: SharedPreferences
    private var accountType = ""
    private var accountID = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)

        replaceFragment(NewsFragment())
        binding.bottomNavigation.setOnNavigationItemSelectedListener() {
            when(it.itemId) {
                R.id.home_page -> replaceFragment(newsFragment)
                R.id.login_page -> replaceFragment(loginFragment)
            }
            true
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(newsFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, fragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("mainFragment", "ui/main resumed")
        var mainActivity = requireActivity() as MainActivity
        var navigationView = mainActivity.navigationView
        var drawerLayout = mainActivity.drawerLayout
        var actionBar = mainActivity.actionBarDrawerToggle

        sharedPref = requireActivity().getSharedPreferences("login_S", AppCompatActivity.MODE_PRIVATE)
        accountType = sharedPref.getString("accountType" , null).toString()
        accountID = sharedPref.getString("accountID" , null).toString()

        if (accountID.toString() != "null") {
            Log.i("mainFragment", "not null:" + accountID.toString())
            binding.bottomNavigation.visibility = BottomNavigationView.GONE

            when(accountType) {
                "donee" -> {
                    navigationView.menu.setGroupVisible(R.id.account_group, true)
                    navigationView.menu.setGroupVisible(R.id.donee_module_group, true)
                }
                "donor" -> {
                    navigationView.menu.setGroupVisible(R.id.account_group, true)
                    navigationView.menu.setGroupVisible(R.id.donor_module_group, true)
                }
                "admin" -> {
                    navigationView.menu.setGroupVisible(R.id.admin_module_group, true)
                }
            }

        } else {
            binding.bottomNavigation.visibility = BottomNavigationView.VISIBLE
            mainActivity.onSupportNavigateUp()
        }

    }

}