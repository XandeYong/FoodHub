package com.example.foodhub.Logged.Admin.FormManagement.Donation

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentAdminDonationFormDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class AdminDonationFormDetailFragment : Fragment() {

    companion object {
        fun newInstance() = AdminDonationFormDetailFragment()
    }

    private lateinit var viewModel: AdminDonationFormDetailViewModel
    private lateinit var binding: FragmentAdminDonationFormDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminDonationFormDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AdminDonationFormDetailViewModel::class.java)


        val preferences =
            this.requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val donationFormID = preferences.getString("donationFormID", null)
        //remove the sharedPref
        preferences.edit().remove("donationFormID").commit()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getAdminDonationForm(requireContext(), donationFormID.toString())
            viewModel.getCategory(requireContext(), viewModel.adminDF.categoryID.toString())

            binding.fieldDonorIdADFD.text = viewModel.adminDF.accountID
            binding.fieldDonationFormIdADFD.text = viewModel.adminDF.donationFormID
            binding.fieldCategoryADFD.text = viewModel.category.name
            binding.fieldFoodADFD.text = viewModel.adminDF.food
            binding.fieldQuantityADFD.text = viewModel.adminDF.quantity.toString()

            //check the position of the status selection
            binding.spinStatusADFD.setSelection(
                viewModel.checkStatusSelectedPosition(
                    resources.getStringArray(
                        R.array.statusListDF
                    )
                )
            )

        }

        binding.btnUpdateADFD.setOnClickListener() {
            displayAlertDialog()
        }

        binding.btnCancelADFD.setOnClickListener() {
            //back to admin list
            findNavController().navigate(AdminDonationFormDetailFragmentDirections.actionAdminDonationFormDetailFragmentToAdminDonationFormListFragment())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun updateStatus(status: String) {
        //check is the status same as previous
        if (status.equals(viewModel.adminDF.status)) {
            Toast.makeText(getActivity(), "Same Status Chosen!", Toast.LENGTH_SHORT).show()
        } else {
            lifecycleScope.launch {
                val value = viewModel.updateStatusToDB(requireContext(), status)
                if (value == 0) {
                    Toast.makeText(getActivity(), "Update Failed!", Toast.LENGTH_SHORT).show()
                } else {
                    updateStatusInRemoteDB()
                }
            }
        }

    }

    private fun displayAlertDialog() {
        //alert dialog
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            updateStatus(binding.spinStatusADFD.selectedItem.toString())
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        }
        val alertDialogBuilder = AlertDialog.Builder(getActivity())
        alertDialogBuilder.setTitle("Confirm Update")
        alertDialogBuilder.setMessage("Are you sure you want to update the status?")
        alertDialogBuilder.setPositiveButton(
            "Yes",
            DialogInterface.OnClickListener(function = positiveButtonClick)
        )
        alertDialogBuilder.setNegativeButton(
            "No",
            DialogInterface.OnClickListener(function = negativeButtonClick)
        )
        alertDialogBuilder.show()

    }

    //Update remote database
    private fun updateStatusInRemoteDB() {

        var url: String = "http://10.0.2.2/foodhub_server/donation_form.php" //put server URL

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->

                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")

                if (status == 0) {
                    Toast.makeText(getActivity(), "Update Success!", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(getActivity(), "Update Failed!", Toast.LENGTH_SHORT).show()
                }

            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    requireContext(),
                    error.toString().trim { it <= ' ' },
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                val data: MutableMap<String, String> = HashMap()
                data["Content-Type"] = "application/x-www-form-urlencoded"
                data["request"] = "UpdateFormStatus"
                data["donationFormID"] = viewModel.adminDF.donationFormID
                data["status"] = binding.spinStatusADFD.selectedItem.toString()

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)

    }


}