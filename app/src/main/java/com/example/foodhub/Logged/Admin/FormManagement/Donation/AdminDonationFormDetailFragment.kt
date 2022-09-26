package com.example.foodhub.Logged.Admin.FormManagement.Donation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodhub.R

class AdminDonationFormDetailFragment : Fragment() {

    companion object {
        fun newInstance() = AdminDonationFormDetailFragment()
    }

    private lateinit var viewModel: AdminDonationFormDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_donation_form_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminDonationFormDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}