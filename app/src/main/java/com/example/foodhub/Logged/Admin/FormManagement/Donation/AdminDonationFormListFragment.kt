package com.example.foodhub.Logged.Admin.FormManagement.Donation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.content.Context
import android.hardware.input.InputManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.databinding.FragmentAdminDonationFormListBinding
import kotlinx.coroutines.launch

class AdminDonationFormListFragment : Fragment() {

    companion object {
        fun newInstance() = AdminDonationFormListFragment()
    }

    // Add RecyclerView member
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentAdminDonationFormListBinding
    private lateinit var viewModel: AdminDonationFormListViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<AdminDonationFormListAdapter.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminDonationFormListBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AdminDonationFormListViewModel::class.java)

        // Add the following lines to create RecyclerView
        recyclerView = binding.recyclerViewADFL
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        myAdapter = AdminDonationFormListAdapter()
        recyclerView.adapter = myAdapter


        lifecycleScope.launch {
            viewModel.getAdminDonationFormList(requireContext())
        }
        viewModel.adminDFL.observe(viewLifecycleOwner, Observer { adminDFL ->
            (myAdapter as AdminDonationFormListAdapter).setData(adminDFL)
            //set toast if empty list
            if (myAdapter.getItemCount() == 0)
            {
                Toast.makeText(getActivity(), "No Donation List Found!", Toast.LENGTH_SHORT).show()
            }
        })

        //set onlClick to Admin Donation Form Detail
        (myAdapter as AdminDonationFormListAdapter).setOnClickListener(object : AdminDonationFormListAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val preferences = requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
                val editor =preferences.edit()
                editor.putString("donationFromID",  (myAdapter as AdminDonationFormListAdapter).adminDonationFormList[position].donationFromID)
                editor.apply()
                editor.commit()
                findNavController().navigate(AdminDonationFormListFragmentDirections.actionAdminDonationFormListFragmentToAdminDonationFormDetailFragment())
            }
        })
        binding.btnSearchADFL.setOnClickListener() {
//            it.hide()
            search()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun search(){
        if(validateSearchInput()){
            viewModel. searchAdminDonationForm(requireContext(), binding.editSearchADFL.text.trim().toString())

            viewModel.adminDFL.observe(viewLifecycleOwner, Observer { adminDFL ->
                (myAdapter as AdminDonationFormListAdapter).setData(adminDFL)
                //set toast if empty list
                if (myAdapter.getItemCount() == 0)
                {
                    Toast.makeText(getActivity(), "No Donation List Found!", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Toast.makeText(getActivity(), "Search Field Is Empty!", Toast.LENGTH_SHORT).show()
        }


    }

    fun validateSearchInput(): Boolean {
        val status = binding.editSearchADFL.text.isNotEmpty()
        return status
    }

//    fun View.hideKeyboard() {
//        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputManager
//        inputManager.(windowToken, 0)
//    }


}