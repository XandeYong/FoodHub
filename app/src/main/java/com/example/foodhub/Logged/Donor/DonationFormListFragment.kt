package com.example.foodhub.Logged.Donor

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.databinding.FragmentDonationFormListBinding
import kotlinx.coroutines.Dispatchers
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
    private lateinit var myAdapter: RecyclerView.Adapter<DonationFormListAdapter.ViewHolder>
    private lateinit var donorID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonationFormListBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(DonationFormListViewModel::class.java)

        // Add the following lines to create RecyclerView
        recyclerView = binding.recyclerViewDFL
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        myAdapter = DonationFormListAdapter()
        recyclerView.adapter = myAdapter

        //Set Donor ID get from darren
        donorID = "DO1"

        lifecycleScope.launch {
            viewModel.getDonationFormList(requireContext(), donorID)
        }
        viewModel.donationFL.observe(viewLifecycleOwner, Observer { donationFL ->
            (myAdapter as DonationFormListAdapter).setData(donationFL)
            //set toast if empty list
            if (myAdapter.getItemCount() == 0)
            {
                Toast.makeText(getActivity(), "No Donation List Found!", Toast.LENGTH_SHORT).show()
            }
        })

        //set onlClick to Donation Form Detail
        (myAdapter as DonationFormListAdapter).setOnClickListener(object : DonationFormListAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(requireContext(), "You clicked on $position", Toast.LENGTH_LONG).show() //need removed
                val preferences = requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
                val editor =preferences.edit()
                editor.putString("donationFromID",  (myAdapter as DonationFormListAdapter).donationFormList[position].donationFromID)
                editor.apply()
                editor.commit()
                findNavController().navigate(DonationFormListFragmentDirections.actionDonationFormListFragmentToDonationFormDetailFragment())
            }
        })

        binding.floatingActionBtnDFL.setOnClickListener {
            findNavController().navigate(DonationFormListFragmentDirections.actionDonationFormListFragmentToDonationFormFragment())
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}