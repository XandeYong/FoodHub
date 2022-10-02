package com.example.foodhub.Logged.Donor

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.databinding.FragmentDonationFormDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class DonationFormDetailFragment : Fragment() {

    companion object {
        fun newInstance() = DonationFormDetailFragment()
    }

    private lateinit var viewModel: DonationFormDetailViewModel
    private lateinit var binding: FragmentDonationFormDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonationFormDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(DonationFormDetailViewModel::class.java)

        val preferences = this.requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val donationFormID =  preferences.getString("donationFormID", null)
        //remove the sharedPref
        preferences.edit().remove("donationFormID").commit()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getDonationForm(requireContext(), donationFormID.toString())
            viewModel.getCategory(requireContext(), viewModel.donationF.categoryID.toString())

            binding.fieldDonationFormIdDFD.text = viewModel.donationF.donationFormID
            binding.fieldCategoryDFD.text = viewModel.category.name
            binding.fieldFoodDFD.text = viewModel.donationF.food
            binding.fieldQuantityDFD.text = viewModel.donationF.quantity.toString()
            binding.fieldStatusDFD.text = viewModel.donationF.status

            if (viewModel.donationF.status == "Donated") {
                binding.btnDeleteDFD.visibility = Button.GONE
            } else {
                binding.btnDeleteDFD.visibility = Button.VISIBLE
            }

        }

        binding.btnDeleteDFD.setOnClickListener() {
            displayAlertDialog()
        }

        binding.btnCancelDFD.setOnClickListener() {
            //Back To Donation Form List
            findNavController().navigate(DonationFormDetailFragmentDirections.actionDonationFormDetailFragmentToDonationFormListFragment())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun displayAlertDialog(){
        //alert dialog
        val positiveButtonClick ={ dialog: DialogInterface, which: Int ->
            deleteDonationForm()
        }
        val negativeButtonClick ={ dialog: DialogInterface, which: Int ->
        }
        val alertDialogBuilder= AlertDialog.Builder(getActivity())
        alertDialogBuilder.setTitle("Confirm Submit")
        alertDialogBuilder.setMessage("Are you sure you want to delete this form?")
        alertDialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
        alertDialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))
        alertDialogBuilder.show()

    }

    private fun deleteDonationForm(){

        lifecycleScope.launch {
            val value = viewModel.updateStatusToDB(requireContext())
            if(value == 0){
                Toast.makeText(getActivity(), "Delete Failed!", Toast.LENGTH_SHORT).show()
            }else{
                updateStatusInRemoteDB()
            }
        }
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
                    Toast.makeText(getActivity(), "Delete Success!", Toast.LENGTH_SHORT).show()

                    //Back To Donation Form List
                    findNavController().navigate(DonationFormDetailFragmentDirections.actionDonationFormDetailFragmentToDonationFormListFragment())

                }else {
                    Toast.makeText(getActivity(), "Delete Failed!", Toast.LENGTH_SHORT).show()
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
                data["donationFormID"] = viewModel.donationF.donationFormID
                data["status"] = "Deleted"
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)

    }

}