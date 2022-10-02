package com.example.foodhub.Logged.All.Account.Profile


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.encodeToString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.R
import com.example.foodhub.database.Account
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.FragmentEditProfileBinding
import com.example.foodhub.util.Util
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
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

    //Account Class Dummy
    private lateinit var accountClass: Account


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
            accountClass = db.accountDao.getLatest()

            //User's basic credentials
            userPassword = accountClass.password.toString()

            //Profile Image
            userImage = accountClass.image
            binding.profileImage.setImageBitmap(userImage)

            //Profile name//
            binding.nameEditText.setText(accountClass.name.toString())

            //Profile email//
            userEmail = accountClass.email.toString()
            binding.emailText.setText(userEmail)

            //Profile password//
            userPassword = accountClass.password.toString()
            binding.passwordText.setText(userPassword)

            //Profile address//
            binding.addressText.setText(accountClass.address.toString())

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
            userBirthday = accountClass.dob
            val dateFormat = SimpleDateFormat("MM-dd-yyyy")
            val yearFormat = SimpleDateFormat("yyyy")

            binding.ageText.setText(dateFormat.format(userBirthday).toString())

            binding.buttonCalendar.setOnClickListener() {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                datePicker.show(childFragmentManager,"datePicker")
                datePicker.addOnPositiveButtonClickListener {

                    //Validate birthday
                    if((Calendar.getInstance().get(Calendar.YEAR) - yearFormat.format(it).toInt()) <= 0){
                        Toast.makeText(requireContext(), "Invalid birthday date!",Toast.LENGTH_LONG).show();
                    }else{
                        userBirthday = Date(it)
                        binding.ageText.setText(dateFormat.format(userBirthday))
                    }
                }
            }

            //Set gender//
            userGender = accountClass.gender.toString()
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
                //Important info validation
                userName = binding.nameEditText.text.toString().trim()
                userEmail = binding.emailText.text.toString().trim()
                userPassword = binding.passwordText.text.toString().trim()

                //Variables turns when done checking
                var dataCheck: Boolean = true


                //Validating Names
                if(userName.isNullOrBlank()){
                    dataCheck = false
                    Toast.makeText(requireContext(), "Please enter your name!",Toast.LENGTH_LONG).show();
                }

                //Validating Email
                val emailPatten: Regex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

                if(!userEmail.matches(emailPatten)){
                    dataCheck = false
                    Toast.makeText(requireContext(), "Invalid email!",Toast.LENGTH_LONG).show();
                }

                //Validating Password
                if(userPassword.toString().isNullOrBlank()){
                    dataCheck = false
                    Toast.makeText(requireContext(), "Please enter you password!",Toast.LENGTH_LONG).show();
                }

                //Prompts Confirmation Dialog after validating
                if(dataCheck){
                    //Confirmation Dialog
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("Confirm update ?")
                        .setCancelable(false)
                        .setPositiveButton("Update"){ _, _->
                            //Passed data from Edit Text
                            userAddress = binding.addressText.text.toString().trim()

                            //Created Account Object for Update
                            accountClass = Account(accountClass.accountID, userName, userImage, userAddress, "S12", userBirthday, userGender,
                                userEmail, userPassword, accountClass.accountType.toString(), accountClass.createdAt, util.generateDate())


                            lifecycleScope.launch{
                                //Update to DB
                                db.accountDao.updateAt(accountClass)

                                //Update into remote DB
                                updateProfileInRemoteDB(accountClass)

                                //Updated Snack Bar Notification
                                Snackbar.make(requireActivity().findViewById(R.id.profileFragment),"Profile Updated!",Snackbar.LENGTH_LONG)
                                    .setAction("Dismiss"){
                                        //Empty to dismiss Snack Bar
                                    }.show()

                                //Redirect to Previous Activity
                                findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
                            }
                        }
                        .setNegativeButton("Cancel"){ dialog, id->
                            //Dismiss dialog
                            dialog.dismiss()
                        }

                    //Show Dialog Box
                    builder.create().show()
                }

            }

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]

    }

    //Process update into Remote DB
    private fun updateProfileInRemoteDB(account: Account) {

        //URL String
        var url = "http://10.0.2.2/foodhub_server/account.php"

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url, Response.Listener { response ->

                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")
                val message = jsonResponse.getString("message")


                if (status == 0) {
                    Log.i("RemoteRequest", message)
                }else {
                    Log.i("RemoteRequest", message)
                }

            },
            Response.ErrorListener { error ->
                Log.i("RemoteError",error.toString().trim { it <= ' ' })
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                val data: MutableMap<String, String> = HashMap()
                data["Content-Type"] = "application/x-www-form-urlencoded"
                data["request"] = "UpdateAccount"
                data["accountID"] = account.accountID
                data["accountName"] = account.name.toString()
                data["accountImage"] = bitmapToString(account.image!!)
                data["accountAddress"] = account.address.toString()
                data["accountState"] = account.state.toString()
                data["accountDOB"] = SimpleDateFormat("yyyy-MM-dd").format(account.dob).toString()
                data["accountGender"] = account.gender.toString()
                data["accountEmail"] = account.email.toString()
                data["accountPassword"] = account.password.toString()
                data["accountType"] = account.accountType.toString()

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    //Bitmap to String Function
    private fun bitmapToString(image: Bitmap): String{
        val boas = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, boas)
        val b = boas.toByteArray()
        return Base64.getEncoder().encodeToString(b)
    }

    //String to Bitmap Function
    private fun stringToBitmap(image: String): Bitmap{
        val decodedString = Base64.getDecoder().decode(image)
        return BitmapFactory.decodeByteArray(decodedString,0,decodedString.size)
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