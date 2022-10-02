package com.example.foodhub.Logged.Admin.FormManagement.Request

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.databinding.FragmentAdminRequestFormListBinding
import kotlinx.coroutines.launch

class AdminRequestFormListFragment : Fragment() {

    companion object {
        fun newInstance() = AdminRequestFormListFragment()
    }
    // Add RecyclerView member
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentAdminRequestFormListBinding
    private lateinit var viewModel: AdminRequestFormListViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<AdminRequestFormListAdapter.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminRequestFormListBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AdminRequestFormListViewModel::class.java)

        // Add the following lines to create RecyclerView
        recyclerView = binding.recyclerViewARFL
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        myAdapter = AdminRequestFormListAdapter()
        recyclerView.adapter = myAdapter

        lifecycleScope.launch {
            viewModel.getAdminRequestFormList(requireContext())
        }
        viewModel.adminRFL.observe(viewLifecycleOwner, Observer { adminRFL ->
            (myAdapter as AdminRequestFormListAdapter).setData(adminRFL)
            //set toast if empty list
            if (myAdapter.getItemCount() == 0)
            {
                Toast.makeText(getActivity(), "No Request List Found!", Toast.LENGTH_SHORT).show()
            }
        })


        //set onlClick to Admin Request Form Detail
        (myAdapter as AdminRequestFormListAdapter).setOnClickListener(object : AdminRequestFormListAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val preferences = requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
                val editor =preferences.edit()
                editor.putString("requestFromID",  (myAdapter as AdminRequestFormListAdapter).adminRequestFormList[position].requestFormID)
                editor.apply()
                editor.commit()
                findNavController().navigate(AdminRequestFormListFragmentDirections.actionAdminRequestFormListFragmentToAdminRequestFormDetailFragment())
            }
        })

        binding.btnSearchARFL.setOnClickListener() {
            it.hideKeyboard()
            search()
        }

        binding.editSearchARFL.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if(s.isEmpty()){
                        lifecycleScope.launch {
                            viewModel.getAdminRequestFormList(requireContext())
                        }
                        viewModel.adminRFL.observe(viewLifecycleOwner, Observer { adminRFL ->
                            (myAdapter as AdminRequestFormListAdapter).setData(adminRFL)
                            //set toast if empty list
                            if (myAdapter.getItemCount() == 0)
                            {
                                Toast.makeText(getActivity(), "No Request List Found!", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                //TODO("Not yet implemented")
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun search(){
        if(validateSearchInput()){
            viewModel.searchAdminRequestForm(requireContext(), binding.editSearchARFL.text.toString().uppercase().trim())

            viewModel.adminRFL.observe(viewLifecycleOwner, Observer { adminRFL ->
                (myAdapter as AdminRequestFormListAdapter).setData(adminRFL)
                //set toast if empty list
                if (myAdapter.getItemCount() == 0)
                {
                    Toast.makeText(getActivity(), "No Request List Found!", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Toast.makeText(getActivity(), "Search Field Is Empty!", Toast.LENGTH_SHORT).show()
        }

    }

    fun validateSearchInput(): Boolean {
        val status = binding.editSearchARFL.text.isNotEmpty()
        return status
    }


    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}