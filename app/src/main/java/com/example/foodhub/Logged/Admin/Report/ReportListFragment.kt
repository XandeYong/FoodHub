package com.example.foodhub.Logged.Admin.Report

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentReportListBinding

class ReportListFragment : Fragment() {

    companion object {
        fun newInstance() = ReportListFragment()
    }

    private lateinit var binding: FragmentReportListBinding
    private lateinit var viewModel: ReportListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportListBinding.inflate(inflater)

        binding.analysisReportButton.setOnClickListener {
            findNavController().navigate(ReportListFragmentDirections.actionReportListFragmentToAnalysisReportFragment())
        }

        binding.locationReportButton.setOnClickListener {
            findNavController().navigate(ReportListFragmentDirections.actionReportListFragmentToLocationReportFragment())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReportListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}