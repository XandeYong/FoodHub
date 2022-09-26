package com.example.foodhub

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodhub.databinding.ActivityMainBinding
import com.example.foodhub.databinding.ActivityMainLoggedBinding
import com.example.foodhub.databinding.FragmentLoginBinding
import com.example.foodhub.ui.login.LoginFragment
import com.example.foodhub.ui.login.LoginViewModel
import com.example.foodhub.ui.main.MainFragmentDirections
import com.example.foodhub.ui.news.NewsFragment
import com.google.android.material.navigation.NavigationView

class MainActivityLogged: Fragment() {

    companion object {
        fun newInstance() = MainActivityLogged()
    }

    private lateinit var binding: ActivityMainLoggedBinding

    private lateinit var topAppBar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    private val newsFragment = NewsFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityMainLoggedBinding.inflate(inflater)

        navDrawerSetup()
        replaceFragment(newsFragment)

        /*
        *
        * change the  account value to "donee", "donor", "admin"
        * to start coding your module
        *
        * */

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


        navigationView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.profile -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToAccountFragment())
                //Test create donation form
                R.id.donation_form_list -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToDonationFormFragment())

                //Test donation form list
                //R.id.donation_form_list -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToDonationFormListFragment())

                //Test donation form detail
                //R.id.donation_form_list -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToDonationFormDetailFragment())

              //admin
                //Test admin donation form detail
                //R.id.donation_form_list -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToAdminDonationFormDetailFragment())

                //Test admin donation form list
                //R.id.donation_form_list -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToAdminDonationFormListFragment())

                //Test admin request form list
                //R.id.donation_form_list -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToAdminRequestFormListFragment())


                //(Xande)
                //Test request form list
                //R.id.donation_form_list -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToRequestFormListFragment())

                //Test report
                //R.id.donation_form_list -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToReportFragment())

                //Test location report
                //R.id.donation_form_list -> findNavController().navigate(MainActivityLoggedDirections.actionMainActivityLoggedToLocationReportFragment())


            }
            true
        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, fragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }
    }

    private fun navDrawerSetup() {
        topAppBar = requireActivity().findViewById(R.id.topAppBar)
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout)
        navigationView = requireActivity().findViewById(R.id.navigationView)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            topAppBar,
            R.string.open_nav_drawer,
            R.string.close_nav_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

}