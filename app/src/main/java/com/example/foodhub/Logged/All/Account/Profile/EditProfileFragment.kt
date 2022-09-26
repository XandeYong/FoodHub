package com.example.foodhub.Logged.All.Account.Profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private lateinit var viewModel: EditProfileViewModel

    //View Binding
    private lateinit var binding: FragmentEditProfileBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater)

        val states = resources.getStringArray(R.array.states)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_state, states)
        binding.stateDropDown.setAdapter(arrayAdapter)

        binding.updateButton.setOnClickListener{
            Toast.makeText(activity,"Profile updated!",Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

    }
}