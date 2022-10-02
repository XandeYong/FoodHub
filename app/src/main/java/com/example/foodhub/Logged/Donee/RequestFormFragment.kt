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
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodhub.R
import com.example.foodhub.database.Category
import com.example.foodhub.databinding.FragmentRequestFormBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestFormFragment : Fragment() {

    companion object {
        fun newInstance() = RequestFormFragment()
    }

    private lateinit var viewModel: RequestFormViewModel
    private lateinit var binding: FragmentRequestFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestFormBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(RequestFormViewModel::class.java)

        lifecycleScope.launch {
            viewModel.getLatestReqForm(requireContext())
            viewModel.generateNewReqFormID()

            binding.fieldRequestFormIdRF.text = viewModel.newReqForm.requestFormID
            binding.fieldStatusRF.text = viewModel.newReqForm.status

            viewModel.getCategoryList(requireContext())

            viewModel.categoryList.observe(viewLifecycleOwner, Observer{ categoryL ->

                if (!categoryL.isEmpty()){
                    val adapter = context?.let {
                        ArrayAdapter<Category>(it, android.R.layout.simple_spinner_item)
                    }
                    categoryL?.forEach {
                        adapter?.add(it)
                    }
                    binding.spinCategoryRF.adapter = adapter
                }else{
                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, resources.getStringArray(R.array.categoryEmpty))
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinCategoryRF.adapter = adapter
                }

            })
        }

        binding.btnSubmitRF.setOnClickListener() {
            it.hideKeyboard()
            if(binding.spinCategoryRF.getSelectedItem().toString().equals("No Category")){
                Toast.makeText(context, "Cannot Submit Request Form!", Toast.LENGTH_LONG).show()
            }else{
                submitAction()
            }
        }

        binding.btnCancelRF.setOnClickListener() {
            it.hideKeyboard()
            cancelAction()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun submitAction() {
        viewModel.setValueFromEditTextView(binding.editQuantityRF.text.toString())
        var status: Boolean = true
        if(viewModel.validateQuantity() != true){
            binding.editQuantityRF.setError("Field must be in digit and cannot be left empty!")
            binding.editQuantityRF.requestFocus()
            status = false
        }else{
            binding.editQuantityRF.setError(null)
        }
        if(status){
            displayAlertDialog()
        }
    }

    private fun cancelAction(){
        findNavController().navigate(RequestFormFragmentDirections.actionRequestFormFragmentToRequestFormListFragment())
    }

    private fun displayAlertDialog(){
        val positiveButtonClick ={ dialog: DialogInterface, which: Int ->
            insertReqForm()
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

    private fun insertReqForm(){
        var value:Int = 0
        lifecycleScope.launch(Dispatchers.IO){
            viewModel.getSelectedCategoryID(binding.spinCategoryRF.selectedItem as Category)
            value = viewModel.insertReqFormToDB(requireContext())

            withContext(Dispatchers.Main) {
                if(value != 0 && value != null){
                    Toast.makeText(requireContext(), "Create Success", Toast.LENGTH_SHORT).show()
                    val preferences = requireActivity().getSharedPreferences("sharePref", Context.MODE_PRIVATE)
                    val editor =preferences.edit()
                    editor.putString("reqFormID", viewModel.newReqForm.requestFormID)
                    editor.apply()
                    editor.commit()

                    //Go to Request Form detail
//                    findNavController().navigate(RequestFormFragmentDirections.actionDonationFormFragmentToDonationFormListFragment())

                }else{
                    Toast.makeText(requireContext(), "Create Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}