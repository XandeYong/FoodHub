package com.example.foodhub.Logged.Admin.Report.AnalysisReport

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodhub.R
import com.example.foodhub.database.AnalysisReport
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.FragmentAnalysisReportBinding
import kotlinx.coroutines.launch

class AnalysisReportFragment : Fragment() {

    companion object {
        fun newInstance() = AnalysisReportFragment()
    }

    private lateinit var binding: FragmentAnalysisReportBinding
    private lateinit var viewModel: AnalysisReportViewModel
    private lateinit var db: FoodHubDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisReportBinding.inflate(inflater)

        binding.btnClose.setOnClickListener {
            findNavController().navigate(AnalysisReportFragmentDirections.actionAnalysisReportFragmentToReportListFragment())
        }

        lifecycleScope.launch {
            db = FoodHubDatabase.getInstance(requireContext())

            val analysisReport: AnalysisReport = db.analysisReportDao.getLatest()
            binding.tvTotalDoneeValue.text = analysisReport.totalDonee.toString()
            binding.tvTotalDonorValue.text = analysisReport.totalDonor.toString()
            binding.tvTotalUserValue.text = analysisReport.totalUser.toString()
            binding.tvTotalDonationValue.text = analysisReport.totalDonation.toString()
            binding.tvTotalRequestValue.text = analysisReport.totalRequest.toString()
            binding.tvTotalNewsValue.text = analysisReport.totalNews.toString()

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AnalysisReportViewModel::class.java)
        // TODO: Use the ViewModel
    }

}