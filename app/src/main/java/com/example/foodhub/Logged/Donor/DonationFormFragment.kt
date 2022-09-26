package com.example.foodhub.Logged.Donor

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentDonationFormBinding
import com.example.foodhub.databinding.FragmentLoginBinding
import com.example.foodhub.ui.main.MainFragmentDirections

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
        viewModel = ViewModelProvider(this).get(DonationFormViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun submitAction() {
//        binding.txtDonationFormIdDF.text = "hhhkhhjkhjhjhjhjk"
//
//        binding.editFoodDF.setError("Error");
//        binding.editFoodDF.requestFocus()
        //binding.editDonationFormIdDF.setText("dsdsd")

        //viewModel.food.value = binding.editFoodDF.text.toString()

        //viewModel.setValue(binding.editFoodDF.text.toString())
        //binding.fieldDonationFormIdDF.text =viewModel.food.value


        viewModel.setValue2(binding.editFoodDF.text.toString())
        binding.fieldDonationFormIdDF.text =viewModel.test.toString()

        //displayAlertDialog()
    }

    private fun cancelAction(){

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
        alertDialogBuilder.setMessage("Are you sure yoy want to submit this form?")
        alertDialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
        alertDialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener(function = negativeButtonClick))
        alertDialogBuilder.show()

    }


    }

