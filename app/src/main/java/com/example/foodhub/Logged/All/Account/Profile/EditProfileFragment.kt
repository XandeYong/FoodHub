package com.example.foodhub.Logged.All.Account.Profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
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

    private val pickImage = 100
    private var imageUri: Uri? = null
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

        binding.updateButton.setOnClickListener(){
            Toast.makeText(activity,"Profile updated!",Toast.LENGTH_SHORT).show()
        }

        binding.addProfileButton.setOnClickListener(){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data

            binding.profileImage.setImageURI(imageUri)
        }

    }
}