package com.example.foodhub.Logged.Donee

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodhub.R

class RequestFormFragment : Fragment() {

    companion object {
        fun newInstance() = RequestFormFragment()
    }

    private lateinit var viewModel: RequestFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request_form, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RequestFormViewModel::class.java)
        // TODO: Use the ViewModel
    }

}