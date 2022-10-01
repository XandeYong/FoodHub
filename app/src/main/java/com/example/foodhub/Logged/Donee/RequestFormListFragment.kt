package com.example.foodhub.Logged.Donee

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
import com.example.foodhub.databinding.FragmentRequestFormListBinding
import kotlinx.coroutines.launch

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
    private lateinit var doneeID: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestFormListBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(RequestFormListViewModel::class.java)

        // Add the following lines to create RecyclerView
        recyclerView = binding.recyclerViewRFL
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = RequestFormListAdapter()
        recyclerView.adapter =adapter

        //Set Donee ID get from darren
        doneeID = "DE2"

        lifecycleScope.launch {
            viewModel.getRequestFormList(requireContext(), doneeID)
        }
        viewModel.requestFL.observe(viewLifecycleOwner, Observer { requestFL ->
            (adapter as RequestFormListAdapter).setDataToAdapter(requestFL)
            //set toast if empty list
            if (adapter.getItemCount() == 0)
            {
                Toast.makeText(getActivity(), "No Request List Found!", Toast.LENGTH_SHORT).show()
            }
        })


        (adapter as RequestFormListAdapter).setOnClickListener(object : RequestFormListAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

                val preferences = requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
                val editor =preferences.edit()
                editor.putString("reqFormID",  (adapter as RequestFormListAdapter).requestFormList[position].requestFormID)
                editor.apply()
                editor.commit()
                findNavController().navigate(RequestFormListFragmentDirections.actionRequestFormListFragmentToRequestFormDetailFragment())
            }
        })

        binding.floatingActionBtnRFL.setOnClickListener {
            findNavController().navigate(RequestFormListFragmentDirections.actionRequestFormListFragmentToRequestFormFragment())
        }

        binding.btnSearchRFL.setOnClickListener() {
            it.hideKeyboard()
            searchReqFormID()
        }

        binding.editSearchRFL.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if(s.isEmpty()){
                        lifecycleScope.launch {
                            viewModel.getRequestFormList(requireContext(), doneeID)
                        }
                        viewModel.requestFL.observe(viewLifecycleOwner, Observer { requestFL ->
                            (adapter as RequestFormListAdapter).setDataToAdapter(requestFL)
                            //set toast if empty list
                            if (adapter.getItemCount() == 0)
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

    fun searchReqFormID(){
        if(validateSearchInput()){
            viewModel.searchRF(requireContext(), doneeID, binding.editSearchRFL.text.toString().uppercase().trim())

            viewModel.requestFL.observe(viewLifecycleOwner, Observer { requestFL ->
                (adapter as RequestFormListAdapter).setDataToAdapter(requestFL)
                //set toast if empty list
                if (adapter.getItemCount() == 0)
                {
                    Toast.makeText(getActivity(), "No Request List Found!", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Toast.makeText(getActivity(), "Search Field Is Empty!", Toast.LENGTH_SHORT).show()
        }

    }

    fun validateSearchInput(): Boolean {
        return binding.editSearchRFL.text.isNotEmpty()
    }

    fun View.hideKeyboard() {
        val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        im.hideSoftInputFromWindow(windowToken, 0)
    }

}