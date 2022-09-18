package com.example.foodhub.Logged.Donee.Detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodhub.R

class RequestFormDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RequestFormDetailFragment()
    }

    private lateinit var viewModel: RequestFormDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request_form_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RequestFormDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}