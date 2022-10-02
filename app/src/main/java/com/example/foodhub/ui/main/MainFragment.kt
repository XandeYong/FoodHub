package com.example.foodhub.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentMainBinding
import com.example.foodhub.ui.login.LoginFragment
import com.example.foodhub.ui.news.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.reflect.KClass


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding

    private val newsFragment = NewsFragment()
    private val loginFragment = LoginFragment()
    var loginCredential = false
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
        val sharedPref = requireActivity().getSharedPreferences("login_S", AppCompatActivity.MODE_PRIVATE)
        val accountType =sharedPref.getString("accountType" , null)
        val accountID =sharedPref.getString("accountID" , null)

        if(!accountID.isNullOrEmpty()) {
            loginCredential = true
            Log.i("true1234", accountID)
        }

        var login = loginCredential
        if (login) {
            binding.bottomNavigation.visibility = BottomNavigationView.GONE

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

    private fun <T: Any> addFragmentToBackStack(kClass: KClass<T>, fragment: Fragment) {
        parentFragmentManager.commit {
            replace(com.example.foodhub.R.id.nav_host_fragment, fragment)
            addToBackStack(kClass.java.name)
        }
    }

}