package com.example.foodhub.Logged.All.Account.Profile

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.foodhub.database.Account
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.FragmentProfileBinding
import com.example.foodhub.util.Util
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {

    val util = Util()

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Binding
        binding = FragmentProfileBinding.inflate(inflater)

        //Database
        lifecycleScope.launch {

            //Initiate DB
            val db = FoodHubDatabase.getInstance(requireContext())

            //Get account from DB
            var account = db.accountDao.getLatest()

            //Setting up Profile
            binding.profileImage.load(account.image)
            binding.idText.text = account.accountID
            binding.nameText.text = account.name.toString()
            binding.textAddress.text = account.address.toString()

            //Get Age from DOB
            val ageFormat = SimpleDateFormat("yyyy")
            binding.textAge.text = (Calendar.getInstance().get(Calendar.YEAR).toInt() - ageFormat.format(account.dob).toInt()).toString()

            //Setup Gender
            if(account.gender == "M"){
                binding.textGender.text = "Male"
            }else{
                binding.textGender.text = "Female"
            }
        }

        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

}

//            //Add Account
//            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
//            val date = "22-4-2000"
//
//            db.accountDao.insert(
//            Account("A1", "Adrian Fong", util.getBitmap("http://10.0.2.2/foodhub_server/image/account/A1.jpg", requireContext()), "PV15 Platinum Victory",
//            null, dateFormat.parse(date), "M", "adrain2000@live.com", "12345", "Admin",
//            util.generateDate(),util.generateDate())
//            )