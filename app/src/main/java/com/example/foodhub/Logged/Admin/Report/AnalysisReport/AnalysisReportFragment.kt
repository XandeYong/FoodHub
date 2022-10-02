package com.example.foodhub.Logged.Admin.Report.AnalysisReport

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.Logged.Donor.DonationFormFragmentDirections
import com.example.foodhub.R
import com.example.foodhub.database.Account
import com.example.foodhub.database.AnalysisReport
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.util.Util
import com.example.foodhub.database.LocationReport
import com.example.foodhub.databinding.FragmentAnalysisReportBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class AnalysisReportFragment : Fragment() {

    companion object {
        fun newInstance() = AnalysisReportFragment()
    }

    private lateinit var binding: FragmentAnalysisReportBinding
    private lateinit var viewModel: AnalysisReportViewModel
    private lateinit var db: FoodHubDatabase
    private lateinit var lanalysisReport: List<AnalysisReport>
    val util = Util()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalysisReportBinding.inflate(inflater)
        db = FoodHubDatabase.getInstance(requireContext())
        binding.btnClose.setOnClickListener {
            findNavController().navigate(AnalysisReportFragmentDirections.actionAnalysisReportFragmentToReportListFragment())
        }

//        lifecycleScope.launch {
//            db = FoodHubDatabase.getInstance(requireContext())
//
//            val analysisReport: AnalysisReport = db.analysisReportDao.getLatest()
//            binding.tvTotalDoneeValue.text = analysisReport.totalDonee.toString()
//            binding.tvTotalDonorValue.text = analysisReport.totalDonor.toString()
//            binding.tvTotalUserValue.text = analysisReport.totalUser.toString()
//            binding.tvTotalDonationValue.text = analysisReport.totalDonation.toString()
//            binding.tvTotalRequestValue.text = analysisReport.totalRequest.toString()
//            binding.tvTotalNewsValue.text = analysisReport.totalNews.toString()
//
//        }

        lifecycleScope.launch {
            getData("all")
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AnalysisReportViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //get Data
    private fun getData(type: String?) {
        var analysisReport: AnalysisReport = AnalysisReport()
        lifecycleScope.launch(Dispatchers.IO){
            analysisReport = db.analysisReportDao.getLatest()
        }

        val url = "http://10.0.2.2/foodhub_server/account.php?request=intializedReport"
        val accounts: MutableList<Account> = mutableListOf()

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received location report")
                val jsonArray = response.getJSONArray("data")

                for (i in 0 until jsonArray.length()) {
                    val jsonObj = jsonArray.getJSONObject(i)
                    val jsonState = jsonObj.getString("state")
                    val jsonAccountType = jsonObj.getString("account_type")

                    val account = Account()
                    account.state = jsonState
                    account.accountType = jsonAccountType

                    accounts.add(account)

                }


                    analysisReport.totalDonor = 0
                    analysisReport.totalDonee = 0
                    analysisReport.totalUser = 0

                for (i in 0 until accounts.size) {
                    val accountType = accounts[i].accountType

                            when (accountType) {
                                "donor" -> {
                                    analysisReport.totalDonor =
                                        analysisReport.totalDonor!!.plus(1)
                                }
                                "donee" -> {
                                    analysisReport.totalDonee =
                                        analysisReport.totalDonee!!.plus(1)
                                }
                            }
                            analysisReport.totalUser =
                                analysisReport.totalUser!!.plus(1)

                }



                lifecycleScope.launch(Dispatchers.IO){
                    val totalDonation = db.donationFormDao.getDonationFormRow()
                    analysisReport.totalDonation = totalDonation
                    val totalRequest = db.requestFormDao.getRequestFormRow()
                    analysisReport.totalRequest = totalRequest
                    val totalNews = db.newsDao.getNewsFormRow()
                    analysisReport.totalNews = totalNews
                }

                updateData(analysisReport)

                updateUI(type, analysisReport)


            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(request)

    }


    //update local DB & server
    private fun updateData(analysisReport: AnalysisReport) {
        lifecycleScope.launch {

                analysisReport.updatedAt = util.generateDate()
                db.analysisReportDao.update(analysisReport)

                postData(analysisReport)
        }
    }

    //update UI
    private fun updateUI(type: String?, analysisReport: AnalysisReport) {

            binding.tvTotalUserValue.text = analysisReport.totalUser.toString()
            binding.tvTotalDonorValue.text =analysisReport.totalDonor.toString()
            binding.tvTotalDoneeValue.text = analysisReport.totalDonee.toString()
            binding.tvTotalDonationValue.text = analysisReport.totalDonation.toString()
            binding.tvTotalRequestValue.text = analysisReport.totalRequest.toString()
            binding.tvTotalNewsValue.text = analysisReport.totalNews.toString()

    }


    //Post to server
    private fun postData(analysisReport: AnalysisReport) {

        val url = "http://10.0.2.2/foodhub_server/analysis_report.php"

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->

                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")

                if (status == 0) {
                    Log.i("db_analysis_report_fragment", "Analysis Report updated successful")
                } else if (status < 0) {
                    Log.i("db_analysis_report_fragment", "Analysis Report failed to update")
                } else {
                    Log.e("db_analysis_report_fragment", "Analysis Report failed to update due to error")
                }
            }, { error ->
                VolleyLog.d("db_analysis_report_fragment", "Error: " + error.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                val data: MutableMap<String, String> = HashMap()
                data["Content-Type"] = "application/x-www-form-urlencoded"
                data["request"] = "update"
                data["analysisReportID"] = analysisReport.analysisReportID
                data["totalUser"] = analysisReport.totalUser.toString()
                data["totalDonor"] = analysisReport.totalDonor.toString()
                data["totalDonee"] = analysisReport.totalDonee.toString()
                data["totalDonation"] = analysisReport.totalDonation.toString()
                data["totalRequest"] = analysisReport.totalRequest.toString()
                data["totalNews"] = analysisReport.totalNews.toString()

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)

    }

}