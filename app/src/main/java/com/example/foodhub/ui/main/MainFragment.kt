package com.example.foodhub.ui.main

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentMainBinding
import com.example.foodhub.ui.login.LoginFragment
import com.example.foodhub.ui.news.NewsFragment
import com.example.foodhub.ui.register.RegisterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.header_navigation_drawer.*
import kotlinx.coroutines.launch


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


}