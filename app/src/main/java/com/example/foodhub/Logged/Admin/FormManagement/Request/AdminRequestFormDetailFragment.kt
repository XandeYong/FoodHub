package com.example.foodhub.Logged.Admin.FormManagement.Request

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodhub.R

class AdminRequestFormDetailFragment : Fragment() {

    companion object {
        fun newInstance() = AdminRequestFormDetailFragment()
    }

    private lateinit var viewModel: AdminRequestFormDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_request_form_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AdminRequestFormDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}