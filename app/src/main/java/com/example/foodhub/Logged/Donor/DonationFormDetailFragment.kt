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
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.foodhub.databinding.FragmentDonationFormDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val donationFromID =  preferences.getString("donationFromID", null)
        //remove the sharedPref
        preferences.edit().remove("donationFromID").commit()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getDonationForm(requireContext(), donationFromID.toString())
            viewModel.getCategory(requireContext(), viewModel.donationF.categoryID.toString())

            binding.fieldDonationFormIdDFD.text = viewModel.donationF.donationFromID
            binding.fieldCategoryDFD.text = viewModel.category.category
            binding.fieldFoodDFD.text = viewModel.donationF.food
            binding.fieldQuantityDFD.text = viewModel.donationF.quantity.toString()
            binding.fieldStatusDFD.text = viewModel.donationF.status

        }

        binding.btnDeleteDFD.setOnClickListener() {
            displayAlertDialog()
        }

        binding.btnCancelDFD.setOnClickListener() {

//            findNavController().navigate(AdminDonationFormListFragmentDirections.actionAdminDonationFormListFragmentToAdminDonationFormDetailFragment())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun displayAlertDialog(){
        //alert dialog
        val positiveButtonClick ={ dialog: DialogInterface, which: Int ->
            deleteDonationForm(viewModel.donationF.donationFromID)
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

    private fun deleteDonationForm(donationFormID: String){
        //add action to change status only

    }

}