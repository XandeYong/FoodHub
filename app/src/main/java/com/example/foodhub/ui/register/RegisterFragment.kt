package com.example.foodhub.ui.register


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.databinding.FragmentRegisterBinding
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var URL: String = "http://10.0.2.2/foodhub_server/"

    var email: String = ""
    var password: String = ""
    var confirmPassword: String = ""
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
            email = binding.txtEmail.text.toString().trim()
            password = binding.txtPassword.text.toString().trim()
            confirmPassword = binding.txtConfirmPassword.text.toString().trim()

            if (email.isNullOrEmpty() || password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()) {
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
            } else {
                if (email.matches(emailPattern.toRegex())) {
                    if (password != confirmPassword) {
                        binding.txtConfirmPassword.error = "Must be same as password"
                    } else {
                        register()
                    }
                } else {
                    binding.txtEmail.error = "Incorrect Email"
                }
            }
//            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragment2ToLoginFragment2())
        }

        return binding.root
    }

    fun register() {
        var accountType: String = ""
        accountType = returnSelectType()


        val getAccountUrl =  URL + "account.php?request=registerGetId&accountType=$accountType"
        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, getAccountUrl, null,
            { response ->
                Log.i("YES1234" , response.toString())

                val jsonArray = response.getJSONObject("data")
                val id = jsonArray.getString("account_id")
                var newID = generateNewAccountID(id)

                register_generate(newID)


            }, { error ->
                Log.d("response123", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(requireActivity())
        requestQueue.add(request)
    }

    fun generateNewAccountID(id: String): String {
        var valueID: String = ""
        var newID: String = ""
        if (binding.btnDonee.isChecked) {
            newID = "DE1"
        } else {
            newID = "DO1"
        }
        if (id != null) {

            if (binding.btnDonee.isChecked) {
                valueID = "DE"
            } else {
                valueID = "DO"
            }
        }
        val value: Int = id.substring(2).toInt() + 1
        newID = valueID + value.toString()
        return newID
    }

    fun register_generate(id: String) {
        var status = false;
        var newName: String = ""

        var accountType: String = returnSelectType()
        newName = generateName(id)
        val registerPostUrl =  URL + "account.php"
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, registerPostUrl,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")

                if (status == 0) {
                    val ToLoginData =
                        this.requireActivity().getSharedPreferences("ToLogin", Context.MODE_PRIVATE)
                    val ToLoginDataStore = ToLoginData?.edit()

                    ToLoginDataStore?.putString("email", email)
                    ToLoginDataStore?.putString("password", password)
                    ToLoginDataStore?.apply()
                    ToLoginDataStore?.commit()


                    Toast.makeText(context, "Register Successfully", Toast.LENGTH_LONG).show()
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMainFragment())
                }else {
                    Toast.makeText(context, "This Email Account Was Registered, Try Again!", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Log.d("ErrorInExceptiom" ,error.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                val data: MutableMap<String, String> = HashMap()
                data["Content-Type"] = "application/x-www-form-urlencoded"
                data["request"] = "register"
                data["id"] = id
                data["name"] = newName
                data["email"] = email
                data["password"] = password
                data["accountType"] = accountType
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    fun generateName(id: String): String {
        var genenteName: String
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val formatted = current.format(formatter)
        genenteName = id.substring(2)
        genenteName += formatted.toString()
        Log.i("12345", genenteName)
        return genenteName
    }

    fun returnSelectType(): String {
        var accountType: String = ""
        if (binding.btnDonee.isChecked) {
            accountType = "donee"
        } else {
            accountType = "donor"
        }
        Log.i("SelectedAccountTpye" , accountType)
        return accountType
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

}
