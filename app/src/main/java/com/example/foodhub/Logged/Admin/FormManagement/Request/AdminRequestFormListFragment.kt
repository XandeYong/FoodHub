package com.example.foodhub.Logged.Admin.FormManagement.Request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.databinding.FragmentAdminRequestFormListBinding

class AdminRequestFormListFragment : Fragment() {

    companion object {
        fun newInstance() = AdminRequestFormListFragment()
    }
    // Add RecyclerView member
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentAdminRequestFormListBinding
    private lateinit var viewModel: AdminRequestFormListViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<AdminRequestFormListAdapter.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminRequestFormListBinding.inflate(inflater)

        // Add the following lines to create RecyclerView
        recyclerView = binding.recyclerViewARFL
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = AdminRequestFormListAdapter()
        recyclerView.adapter =adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminRequestFormListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}