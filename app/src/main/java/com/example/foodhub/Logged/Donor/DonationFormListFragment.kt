package com.example.foodhub.Logged.Donor

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.database.DonationForm
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.FragmentDonationFormListBinding
import kotlinx.coroutines.launch


class DonationFormListFragment : Fragment() {

    companion object {
        fun newInstance() = DonationFormListFragment()
    }

    // Add RecyclerView member
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentDonationFormListBinding
    private lateinit var viewModel: DonationFormListViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<DonationFormListAdapter.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonationFormListBinding.inflate(inflater)

//        var data: LiveData<List<DonationForm>>
//        lifecycleScope.launch {
//            data = viewModel.getData(requireContext())
//
//            Log.i("DonationForm data", data.value.toString())
//            for(i in 0 until data.value!!.size) {
//                val df = data.value
//                Log.i("DonationForm data", df!![i].food.toString())
//            }
//        }



        // Add the following lines to create RecyclerView
        recyclerView = binding.recyclerViewDFL
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = DonationFormListAdapter()
        recyclerView.adapter =adapter

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DonationFormListViewModel::class.java)
        // TODO: Use the ViewModel

        Log.i("DonationForm data", "test")
        var db = FoodHubDatabase.getInstance(requireContext())

        lifecycleScope.launch {
            val df:DonationForm = viewModel.getData(requireContext())

            Log.i("DF", df.accountID.toString())
        }

    }

}