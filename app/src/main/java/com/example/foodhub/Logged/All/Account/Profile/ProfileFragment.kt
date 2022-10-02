package com.example.foodhub.Logged.All.Account.Profile


import android.os.Bundle
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

            if(account.address.toString().isNullOrEmpty() || account.address.toString() == "null"){
                binding.textAddress.text = ""
            }else{
                binding.textAddress.text = account.address.toString()
            }


            //Get Age from DOB
            val ageFormat = SimpleDateFormat("yyyy")
            binding.textAge.text = (Calendar.getInstance().get(Calendar.YEAR) - ageFormat.format(account.dob).toInt()).toString()

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