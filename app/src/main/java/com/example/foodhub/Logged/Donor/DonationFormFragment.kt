package com.example.foodhub.Logged.Donor

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodhub.Logged.Admin.FormManagement.Donation.AdminDonationFormListAdapter
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentDonationFormBinding
import com.example.foodhub.databinding.FragmentLoginBinding
import com.example.foodhub.ui.main.MainFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DonationFormFragment : Fragment() {

    companion object {
        fun newInstance() = DonationFormFragment()
    }

    private lateinit var binding: FragmentDonationFormBinding
    private lateinit var viewModel: DonationFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonationFormBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(DonationFormViewModel::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getLatestDonationForm(requireContext())
            viewModel.generateNewDonationFormID()

            binding.fieldDonationFormIdDF.text = viewModel.newDonationForm.donationFromID
            binding.fieldStatusDF.text = viewModel.newDonationForm.status

            viewModel.getCategoryList(requireContext())

            if ( viewModel.categoryList.isEmpty()){
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, resources.getStringArray(R.array.categoryEmpty))
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinCategoryDF.adapter = adapter

            }else{
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, viewModel.categoryList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinCategoryDF.adapter = adapter
            }

        }

        binding.btnSubmitDF.setOnClickListener() {
            submitAction()
        }

        binding.btnCancelDF.setOnClickListener() {
            cancelAction()
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun submitAction() {
        viewModel.setValueFromEditTextView(binding.editFoodDF.text.toString(), binding.editQuantityDF.text.toString())
        var status: Boolean = true
        if(viewModel.validateFood() != true){
            binding.editFoodDF.setError("Field must be in alphabet and cannot be left empty!")
            binding.editFoodDF.requestFocus()
            status = false
        }else{
            binding.editFoodDF.setError(null)
        }

        if(viewModel.validateQuantity() != true){
            binding.editQuantityDF.setError("Field must be in digit and cannot be left empty!")
            binding.editQuantityDF.requestFocus()
            status = false
        }else{
            binding.editQuantityDF.setError(null)
        }

        if(status){
            displayAlertDialog()
        }

    }

    private fun cancelAction(){
//back to admin list
//            findNavController().navigate(AdminDonationFormListFragmentDirections.actionAdminDonationFormListFragmentToAdminDonationFormDetailFragment())
    }

    private fun displayAlertDialog(){
        //alert dialog
        val positiveButtonClick ={ dialog: DialogInterface, which: Int ->
            Toast.makeText(context, "Confirmed", Toast.LENGTH_LONG).show()

        }
        val negativeButtonClick ={ dialog: DialogInterface, which: Int ->
            Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show()

        }
        val alertDialogBuilder= AlertDialog.Builder(getActivity())
        alertDialogBuilder.setTitle("Confirm Submit")
        alertDialogBuilder.setMessage("Are you sure you want to submit this form?")
        alertDialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
        alertDialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))
        alertDialogBuilder.show()

    }

    private fun insertDonationForm(){
//        viewModel.insetDonationFormToDB(requireContext())
    }


}

