package com.example.foodhub.Logged.Admin.News

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodhub.databinding.FragmentUpdateNewsAdminBinding

class updateNews_admin : Fragment() {

    companion object {
        fun newInstance() = updateNews_admin()
    }

    private lateinit var viewModel: UpdateNewsAdminViewModel
    private lateinit var binding: FragmentUpdateNewsAdminBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateNewsAdminBinding.inflate(inflater)

        val preferences = this.requireActivity().getSharedPreferences("pass", Context.MODE_PRIVATE)
        val test =  preferences.getString("id", null)

        binding.btnUpdate.setOnClickListener{
//            confirmDialogBox()
            viewModel.startTypeSomethings()
        }

        return binding.root
    }

    fun confirmDialogBox(){
        var buidle = AlertDialog.Builder(context)
        buidle.setTitle("Confirm Delete")
        buidle.setMessage("Are you want to delete this news ?")
        buidle.setPositiveButton("Yes" , DialogInterface.OnClickListener{dialog, id ->

            Toast.makeText(requireContext(), "Delete Successfully" , Toast.LENGTH_LONG).show()
            dialog.cancel()
            // need navigate back to the receiver
        })
        buidle.setNegativeButton("No" , DialogInterface.OnClickListener{dialog, id ->

            Toast.makeText(requireContext(), "NO Change" , Toast.LENGTH_LONG).show()
            dialog.cancel()

        })

        var alert = buidle.create()
        alert.show()
    // need navigate back to the receiver
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateNewsAdminViewModel::class.java)
        // TODO: Use the ViewModel

        viewModel.typeSomethings.observe(viewLifecycleOwner, Observer{
            binding.lblTittle.text = it.toString()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }
}