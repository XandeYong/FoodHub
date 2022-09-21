package com.example.foodhub.Logged.Donee

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentDonationFormListBinding

class DonationFormListFragment : Fragment() {

    companion object {
        fun newInstance() = DonationFormListFragment()
    }

    private lateinit var binding: FragmentDonationFormListBinding
    private lateinit var viewModel: DonationFormListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonationFormListBinding.inflate(inflater)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DonationFormListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}