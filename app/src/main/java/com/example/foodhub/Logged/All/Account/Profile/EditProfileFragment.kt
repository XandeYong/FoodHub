package com.example.foodhub.Logged.All.Account.Profile


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodhub.R
import com.example.foodhub.database.Account
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.FragmentEditProfileBinding
import com.example.foodhub.util.Util
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    val util = Util()

    //View Model
    private lateinit var viewModel: EditProfileViewModel

    //View Binding
    private lateinit var binding: FragmentEditProfileBinding

    //Variables for Profile Update
    private var userEmail: String = ""
    private var userPassword: String = ""
    private var pickImage: Int  = 100
    private var imageUri: Uri? = null
    private var userImage: Bitmap? = null
    private var userName: String = ""
    private var userAddress: String = ""
    private var userState: String = ""
    private var userBirthday: Date? = null
    private var userGender: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater)

        //Add Profile Picture
        binding.addProfileButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        lifecycleScope.launch {
            //Initiate DB
            val db = FoodHubDatabase.getInstance(requireContext())

            //Get account from DB
            var account = db.accountDao.getLatest()

            //User's basic credentials
            userEmail = account.email.toString()
            userPassword = account.password.toString()

            //Profile Image
            userImage = account.image
            binding.profileImage.setImageBitmap(userImage)

            //Profile name//
            binding.nameEditText.setText(account.name.toString())

            //Profile address//
            binding.addressText.setText(account.address.toString())

            //Profile state//
            //State Drop down list
            val states = resources.getStringArray(R.array.states)
            val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_state, states)
            binding.stateDropDown.setAdapter(arrayAdapter)

            //Set default value
            userState = "Sabah"//account.stateID.toString()
            binding.stateDropDown.setText(userState,false)

            //Set on click
            binding.stateDropDown.setOnItemClickListener { parent, _, position, _ ->
                var selectedState = parent.getItemAtPosition(position).toString()
                binding.stateDropDown.setText(selectedState,false)
            }

            //Set birthday or Age//
            userBirthday = account.dob
            val dateFormat = SimpleDateFormat("MM-dd-yyyy")
            binding.ageText.setText(dateFormat.format(userBirthday).toString())

            binding.buttonCalendar.setOnClickListener() {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                datePicker.show(childFragmentManager,"datePicker")
                datePicker.addOnPositiveButtonClickListener {
                    userBirthday = Date(it)
                    binding.ageText.setText(dateFormat.format(userBirthday))
                }
            }

            //Set gender//
            userGender = account.gender.toString()
            if(userGender == "M"){
                binding.radioMale.isChecked = true
            }else{
                binding.radioFemale.isChecked = true
            }

            binding.genderRadioGroup.setOnCheckedChangeListener { _, _ ->
                if(binding.radioMale.isChecked){
                    userGender = "M"
                }else if(binding.radioFemale.isChecked){
                    userGender = "F"
                }
            }


            binding.updateButton.setOnClickListener(){

                //Confirmation Dialog
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Confirm update ?")
                    .setCancelable(false)
                    .setPositiveButton("Update"){ dialog, id->
                        //Passed data from Edit Text
                        userName = binding.nameEditText.text.toString()
                        userAddress = binding.addressText.text.toString()

                        //Created Account Object for Update
                        val userAccount = Account(account.accountID, userName, userImage, userAddress, null, userBirthday, userGender,
                            userEmail, userPassword, account.accountType.toString(), account.createdAt, util.generateDate())


                        lifecycleScope.launch{
                            db.accountDao.updateAt(userAccount)

                            Toast.makeText(activity,"Profile updated!",Toast.LENGTH_SHORT).show()

                            findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
                        }
                    }
                    .setNegativeButton("Cancel"){ dialog, id->
                        //Dismiss dialog
                        dialog.dismiss()
                    }

                builder.create().show()
            }

        }



        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

    }


    //Function for accessing gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            //Getting image URI
            imageUri = data?.data

            //Convert URI to Bitmap and store to temporary variable
            userImage = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,imageUri)

            //Load bitmap image via temporary variable
            binding.profileImage.setImageBitmap(userImage)
        }
    }
}