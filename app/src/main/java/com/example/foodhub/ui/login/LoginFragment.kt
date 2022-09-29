package com.example.foodhub.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodhub.databinding.FragmentLoginBinding
import com.example.foodhub.ui.main.MainFragmentDirections
import com.example.foodhub.ui.register.RegisterFragment
import com.example.foodhub.ui.register.RegisterFragmentDirections


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var shared : SharedPreferences
    private var loginAttempt : Int = 0

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.button1.setOnClickListener {

//            if(binding.txtEmail.text.toString().isNullOrEmpty() || binding.txtPasswordLogin.text.toString().isNullOrEmpty()){
//                if(binding.txtEmail.text.toString().isNullOrEmpty()){
//                    binding.txtEmail.error = "Cannot Be Empty"
//                }
//                if(binding.txtPasswordLogin.text.toString().isNullOrEmpty()){
//                    binding.txtPasswordLogin.error = "Cannot Be Empty"
//                }
//            }
//             if (!binding.txtEmail.text.toString().trim().matches(emailPattern.toRegex())) {
//                binding.txtEmail.error = "Incorrect Email"
//            }
//            else {
//                if(binding.txtPasswordLogin.text.toString() != "12" && loginAttempt < 5){
//                    // all the things must get from database
//                    ++loginAttempt
//                    binding.txtPasswordLogin.error = "Password Not Correct"
//                    Toast.makeText(requireContext(), "Invalid Log in Credential. Remaining Attempt : $loginAttempt" , Toast.LENGTH_LONG).show()
//                }else if (binding.txtPasswordLogin.text.toString() == "12" && loginAttempt < 5) {
//                    // here need share preferences
//                    Toast.makeText(context, "Welcome back!" , Toast.LENGTH_LONG).show()
//                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToMainActivityLogged())
//                }
//                else {
//                    binding.txtPasswordLogin.error = "Password Not Correct"
//                    Toast.makeText(this.requireContext(), "Used Out attempt Limit, Pls try Again Later" , Toast.LENGTH_LONG).show()
//                }
//            }
            findNavController().navigate(MainFragmentDirections.actionMainFragmentSelf())


        }
        binding.txtSignUp.setOnClickListener() {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRegisterFragment()) // not working

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

    }

}