package com.example.foodhub.Logged.Admin.FormManagement.Donation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.databinding.FragmentAdminDonationFormListBinding

class AdminDonationFormListFragment : Fragment() {

    companion object {
        fun newInstance() = AdminDonationFormListFragment()
    }

    // Add RecyclerView member
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentAdminDonationFormListBinding
    private lateinit var viewModel: AdminDonationFormListViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<AdminDonationFormListAdapter.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminDonationFormListBinding.inflate(inflater)


        // Add the following lines to create RecyclerView
        recyclerView = binding.recyclerViewADFL
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = AdminDonationFormListAdapter()
        recyclerView.adapter =adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminDonationFormListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}