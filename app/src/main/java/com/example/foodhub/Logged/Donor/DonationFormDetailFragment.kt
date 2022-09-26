package com.example.foodhub.Logged.Donor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodhub.R

class DonationFormDetailFragment : Fragment() {

    companion object {
        fun newInstance() = DonationFormDetailFragment()
    }

    private lateinit var viewModel: DonationFormDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donation_form_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DonationFormDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}