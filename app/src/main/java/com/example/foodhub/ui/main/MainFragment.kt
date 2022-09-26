package com.example.foodhub.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.foodhub.Logged.Donor.DonationFormListFragment
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentMainBinding
import com.example.foodhub.ui.login.LoginFragment
import com.example.foodhub.ui.news.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.KClass


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding

    private val newsFragment = NewsFragment()
    private val loginFragment = LoginFragment()

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

        var login = false
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