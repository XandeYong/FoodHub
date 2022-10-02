package com.example.foodhub.Logged.All.Account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodhub.R
import com.example.foodhub.database.FoodHubDatabase
import kotlinx.android.synthetic.main.activity_main.view.*
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

        sharedPref.edit().clear().apply()
        lifecycleScope.launch {
            db.accountDao.clear()
            Log.d("logout", sharedPref.getString("accountID", null).toString())

            if (requireActivity().actionBar != null) {
                requireActivity().actionBar!!.setDisplayHomeAsUpEnabled(false)
            }

            findNavController().navigate(LogoutFragmentDirections.actionLogoutFragmentToMainFragment())
            requireActivity().supportFragmentManager.popBackStack()
        }

        return inflater.inflate(null, container, false)
    }

}