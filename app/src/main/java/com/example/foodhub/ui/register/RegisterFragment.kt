package com.example.foodhub.ui.register


import android.content.Context
import android.graphics.Color
import android.net.MailTo
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodhub.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        binding.btnDonor.setOnClickListener {
            binding.btnDonor.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnDonee.setTextColor(Color.parseColor("#FF000000"))
        }
        binding.btnDonee.setOnClickListener {
            binding.btnDonee.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.btnDonor.setTextColor(Color.parseColor("#FF000000"))
        }

        binding.btnReg.setOnClickListener {
            val email: String = binding.txtEmail.text.toString().trim()
            if (binding.txtEmail.text.isNullOrEmpty() || binding.txtPassword.text.isNullOrEmpty() || binding.txtConfirmPassword.text.isNullOrEmpty()) {
                if (
                    binding.txtEmail.text.isNullOrEmpty()) {
                    binding.txtEmail.error = "Cannot be empty"
                }
                if (
                    binding.txtPassword.text.isNullOrEmpty()) {
                    binding.txtPassword.error = "Cannot be empty"
                }
                if (
                    binding.txtConfirmPassword.text.isNullOrEmpty()) {
                    binding.txtConfirmPassword.error = "Cannot be empty"
                }
            }else {
                if (email.matches(emailPattern.toRegex())) {
                    if (binding.txtPassword.text.toString() != binding.txtConfirmPassword.text.toString())
                    {
                        binding.txtConfirmPassword.error = "Must be same as password"
                    }
                } else {
                    binding.txtEmail.error = "Incorrect Email"
                }
            }
//            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragment2ToLoginFragment2())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

}
