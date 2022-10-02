package com.example.foodhub.Logged.Donee

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
import com.example.foodhub.databinding.FragmentRequestFormDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class RequestFormDetailFragment : Fragment() {

    companion object {
        fun newInstance() = RequestFormDetailFragment()
    }

    private lateinit var viewModel: RequestFormDetailViewModel
    private lateinit var binding: FragmentRequestFormDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestFormDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(RequestFormDetailViewModel::class.java)

        val preferences = this.requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val reqFormID =  preferences.getString("reqFormID", null)
        preferences.edit().remove("reqFormID").commit()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getReqForm(requireContext(), reqFormID.toString())
            viewModel.getCategory(requireContext(), viewModel.requestForm.categoryID.toString())

            binding.fieldRequestFormIdRFD.text = viewModel.requestForm.requestFormID
            binding.fieldCategoryRFD.text = viewModel.ctg.name
            binding.fieldQuantityRFD.text = viewModel.requestForm.quantity.toString()
            binding.fieldStatusRFD.text = viewModel.requestForm.status

            if (viewModel.requestForm.status == "Fulfill") {
                binding.btnDeleteRFD.visibility = Button.GONE
            } else {
                binding.btnDeleteRFD.visibility = Button.VISIBLE
            }
        }

        binding.btnDeleteRFD.setOnClickListener() {
            displayAlertDialog()
        }

        binding.btnCancelRFD.setOnClickListener() {
            findNavController().navigate(RequestFormDetailFragmentDirections.actionRequestFormDetailFragmentToRequestFormListFragment())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun displayAlertDialog(){

        val positiveButtonClick ={ dialog: DialogInterface, which: Int ->
            deleteRequestForm()
        }
        val negativeButtonClick ={ dialog: DialogInterface, which: Int ->
        }
        val alertDialogBuilder= AlertDialog.Builder(getActivity())
        alertDialogBuilder.setTitle("Confirm Delete")
        alertDialogBuilder.setMessage("Are you sure you want to delete this form?")
        alertDialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
        alertDialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))
        alertDialogBuilder.show()
    }

    private fun deleteRequestForm(){
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

        var url: String = "http://10.0.2.2/foodhub_server/request_form.php" //put server URL

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->

                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")

                if (status == 0) {
                    Toast.makeText(getActivity(), "Delete Success!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(RequestFormDetailFragmentDirections.actionRequestFormDetailFragmentToRequestFormListFragment())
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
                data["requestFormID"] = viewModel.requestForm.requestFormID
                data["status"] = "Deleted"
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)

    }

}