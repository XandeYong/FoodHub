package com.example.foodhub.Logged.Donor

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodhub.R
import com.example.foodhub.database.Category
import com.example.foodhub.databinding.FragmentDonationFormBinding
import kotlinx.android.synthetic.main.fragment_donation_form.*
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

        lifecycleScope.launch {
            viewModel.getLatestDonationForm(requireContext())
            viewModel.generateNewDonationFormID()

            binding.fieldDonationFormIdDF.text = viewModel.newDonationForm.donationFromID
            binding.fieldStatusDF.text = viewModel.newDonationForm.status

            viewModel.getCategoryList(requireContext())

                viewModel.categoryList.observe(viewLifecycleOwner, Observer{ categoryL ->

                    if (!categoryL.isEmpty()){
                        val adapter = context?.let {
                            ArrayAdapter<Category>(it, android.R.layout.simple_spinner_item)
                        }
                        categoryL?.forEach {
                            adapter?.add(it)
                        }
                        binding.spinCategoryDF.adapter = adapter
                    }else{
                        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, resources.getStringArray(R.array.categoryEmpty))
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinCategoryDF.adapter = adapter
                    }

                })



        }

        binding.btnSubmitDF.setOnClickListener() {
            if(binding.spinCategoryDF.getSelectedItem().toString().equals("No Category")){
                Toast.makeText(context, "Cannot Submit Donation Form!", Toast.LENGTH_LONG).show()
            }else{
                submitAction()
            }
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
        //back to donation form list
        findNavController().navigate(DonationFormDetailFragmentDirections.actionDonationFormDetailFragmentToDonationFormListFragment())
    }

    private fun displayAlertDialog(){
        //alert dialog
        val positiveButtonClick ={ dialog: DialogInterface, which: Int ->
                insertDonationForm()
        }
        val negativeButtonClick ={ dialog: DialogInterface, which: Int ->
        }
        val alertDialogBuilder= AlertDialog.Builder(getActivity())
        alertDialogBuilder.setTitle("Confirm Submit")
        alertDialogBuilder.setMessage("Are you sure you want to submit this form?")
        alertDialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
        alertDialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))
        alertDialogBuilder.show()

    }

    private fun insertDonationForm(){
        var value:Int = 0
        lifecycleScope.launch(Dispatchers.IO){
            viewModel.getSelectedCategoryID(binding.spinCategoryDF.selectedItem as Category)
            value = viewModel.insetDonationFormToDB(requireContext())
        }
        if(value != 0 || value != null){
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(requireContext(), "Fail", Toast.LENGTH_SHORT).show()
        }

    }


}

