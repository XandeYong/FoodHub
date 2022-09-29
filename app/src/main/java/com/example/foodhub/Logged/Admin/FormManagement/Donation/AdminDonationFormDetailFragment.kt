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
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentAdminDonationFormDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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


        val preferences = this.requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
        val donationFromID =  preferences.getString("donationFromID", null)
        //remove the sharedPref
        preferences.edit().remove("donationFromID").commit()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getAdminDonationForm(requireContext(), donationFromID.toString())
            viewModel.getCategory(requireContext(), viewModel.adminDF.categoryID.toString())

            binding.fieldDonorIdADFD.text = viewModel.adminDF.accountID
            binding.fieldDonationFormIdADFD.text = viewModel.adminDF.donationFromID
            binding.fieldCategoryADFD.text = viewModel.category.category
            binding.fieldFoodADFD.text = viewModel.adminDF.food
            binding.fieldQuantityADFD.text = viewModel.adminDF.quantity.toString()

            //check the position of the status selection
            binding.spinStatusADFD.setSelection(viewModel.checkStatusSelectedPosition(resources.getStringArray(R.array.statusListDF)))

        }

        binding.btnUpdateADFD.setOnClickListener() {
            displayAlertDialog()
        }

        binding.btnCancelADFD.setOnClickListener() {
//back to admin list
//            findNavController().navigate(AdminDonationFormListFragmentDirections.actionAdminDonationFormListFragmentToAdminDonationFormDetailFragment())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    private fun updateStatus(status: String){
        //check is the status same as previous
        if(status.equals(viewModel.adminDF.status)){
            Toast.makeText(getActivity(), "Same Status Chosen!", Toast.LENGTH_SHORT).show()
        }else{
            lifecycleScope.launch {
                val value = viewModel.updateStatusToDB(requireContext(), status)
                if(value == 0){
                    Toast.makeText(getActivity(), "Update Failed!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(getActivity(), "Update Success!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun displayAlertDialog(){
        //alert dialog
        val positiveButtonClick ={ dialog: DialogInterface, which: Int ->
            updateStatus(binding.spinStatusADFD.selectedItem.toString())
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