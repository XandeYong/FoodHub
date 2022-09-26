package com.example.foodhub.Logged.Donee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.databinding.FragmentRequestFormListBinding

class RequestFormListFragment : Fragment() {

    companion object {
        fun newInstance() = RequestFormListFragment()
    }

    // Add RecyclerView member
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentRequestFormListBinding
    private lateinit var viewModel: RequestFormListViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<RequestFormListAdapter.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestFormListBinding.inflate(inflater)

        // Add the following lines to create RecyclerView
        recyclerView = binding.recyclerViewRFL
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = RequestFormListAdapter()
        recyclerView.adapter =adapter

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RequestFormListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}