package com.example.foodhub.Logged.Admin.FormManagement.Request

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
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentAdminRequestFormDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminRequestFormDetailFragment : Fragment() {

    companion object {
        fun newInstance() = AdminRequestFormDetailFragment()
    }

    private lateinit var viewModel: AdminRequestFormDetailViewModel
    private lateinit var binding: FragmentAdminRequestFormDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminRequestFormDetailBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AdminRequestFormDetailViewModel::class.java)

        val preferences = this.requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val requestFromID =  preferences.getString("requestFromID", null)
        //remove the sharedPref
        preferences.edit().remove("requestFromID").commit()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getAdminRequestForm(requireContext(), requestFromID.toString())
            viewModel.getCategory(requireContext(), viewModel.adminRF.categoryID.toString())

            binding.fieldDoneeIdARFD.text = viewModel.adminRF.accountID
            binding.fieldRequestFormIdARFD.text = viewModel.adminRF.requestFormID
            binding.fieldCategoryARFD.text = viewModel.category.name
            binding.fieldQuantityARFD.text = viewModel.adminRF.quantity.toString()

            //check the position of the status selection
            binding.spinStatusARFD.setSelection(viewModel.checkStatusSelectedPosition(resources.getStringArray(R.array.statusListRF)))

        }

        binding.btnUpdateARFD.setOnClickListener() {
            displayAlertDialog()
        }

        binding.btnCancelARFD.setOnClickListener() {
//back to admin list
//            findNavController().navigate(AdminDonationFormListFragmentDirections.actionAdminDonationFormListFragmentToAdminDonationFormDetailFragment())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun updateStatus(status: String){
        //check is the status same as previous
        if(status.equals(viewModel.adminRF.status)){
            Toast.makeText(getActivity(), "Same Status Chosen!", Toast.LENGTH_SHORT).show()
        }else {
            lifecycleScope.launch {
                val value = viewModel.updateStatusToDB(requireContext(), status)
                if (value == 0) {
                    Toast.makeText(getActivity(), "Update Failed!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getActivity(), "Update Success!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayAlertDialog(){
        //alert dialog
        val positiveButtonClick ={ dialog: DialogInterface, which: Int ->
            updateStatus(binding.spinStatusARFD.selectedItem.toString())
        }
        val negativeButtonClick ={ dialog: DialogInterface, which: Int ->
        }
        val alertDialogBuilder= AlertDialog.Builder(getActivity())
        alertDialogBuilder.setTitle("Confirm Submit")
        alertDialogBuilder.setMessage("Are you sure you want to update the status?")
        alertDialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
        alertDialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))
        alertDialogBuilder.show()

    }

}