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
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodhub.databinding.FragmentRequestFormDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        alertDialogBuilder.setTitle("Confirm Submit")
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
                Toast.makeText(getActivity(), "Delete Success!", Toast.LENGTH_SHORT).show()

                findNavController().navigate(RequestFormDetailFragmentDirections.actionRequestFormDetailFragmentToRequestFormListFragment())
            }
        }
    }

}