package com.example.foodhub.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.database.Account
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.generateDate
import com.example.foodhub.databinding.FragmentLoginBinding
import com.example.foodhub.ui.main.MainFragmentDirections
import com.example.foodhub.util.Util
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.MutableMap
import kotlin.collections.set

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    val util = Util()

    private val URL: String = "http://10.0.2.2/foodhub_server/account.php"

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    var email: String = ""
    var password: String = ""

    var dbId: String = ""
    var dbName: String = ""
    var dbPassword: String = ""
    var dbEmail: String = ""
    var dbAccountType: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        val sharedPref = this.requireActivity().getSharedPreferences("ToLogin", AppCompatActivity.MODE_PRIVATE)
        val passEmail =sharedPref.getString("email" , null)
        val passPassword =sharedPref.getString("password" , null)

        if(!passEmail.isNullOrEmpty()){
            binding.txtEmail.setText("$passEmail")
            binding.txtPasswordLogin.setText("$passPassword")
            sharedPref.edit().remove("email").commit()
            sharedPref.edit().remove("password").commit()

        }else {
            binding.txtEmail.setText("")
            binding.txtPasswordLogin.setText("")
        }

        binding.button1.setOnClickListener {

            email = binding.txtEmail.text.toString()
            password = binding.txtPasswordLogin.text.toString()


            if (email.isNullOrEmpty() || password.isNullOrEmpty()
            ) {
                if (email.isNullOrEmpty()) {
                    binding.txtEmail.error = "Cannot Be Empty"
                }
                if (password.isNullOrEmpty()) {
                    binding.txtPasswordLogin.error = "Cannot Be Empty"
                }
            }
            if (!email.trim().matches(emailPattern.toRegex())) {
                binding.txtEmail.error = "Incorrect Email"
            } else {
                LoginCheck()
            }
        }

        binding.txtSignUp.setOnClickListener() {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRegisterFragment())

        }

        return binding.root
    }


    fun LoginCheck(): Boolean {
        var status = false;

        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener { response ->
                val myobject: JSONObject
                val jsonResponse = JSONObject(response)

                if (jsonResponse.getInt("status") == 0) {
                    val myObjAsString = jsonResponse.getJSONArray("data")
                    myobject = myObjAsString.getJSONObject(0)
                    dbId = myobject.get("account_id").toString()
                    dbName = myobject.get("name").toString()
                    val dbImage = myobject.get("image").toString()
                    val dbAddress = myobject.get("address").toString()
                    val dbState = myobject.get("state").toString()
                    var dbDob = myobject.get("dob").toString()
                    val dbGender = myobject.get("gender").toString()
                    dbEmail = myobject.get("email").toString()
                    dbPassword = myobject.get("password").toString()
                    dbAccountType = myobject.get("account_type").toString()

                    var date = SimpleDateFormat("yyyy-mm-dd").parse(dbDob)

                    val db = FoodHubDatabase.getInstance(requireContext())
                    lifecycleScope.launch {
                        val account = Account(dbId, dbName, util.getBitmap(dbImage, requireContext()), dbAddress, dbState, date, dbGender, dbEmail, dbPassword, dbAccountType, util.generateDate(), util.generateDate())
                        db.accountDao.insert(account)


                        if (password == dbPassword) {
                            val preferences =
                                activity?.getSharedPreferences("login_S", Context.MODE_PRIVATE)
                            val editor = preferences?.edit()

                            editor?.putString("accountID", dbId)
                            editor?.putString("name", dbName)
                            editor?.putString("password", dbPassword)
                            editor?.putString("accountType", dbAccountType)
                            editor?.apply()
                            editor?.commit()

                            Toast.makeText(context, "Welcome back!", Toast.LENGTH_LONG).show()
                            findNavController().navigate(MainFragmentDirections.actionMainFragmentSelf())
                        } else {
                            binding.txtPasswordLogin.error = "Password Not Correct"
                            Toast.makeText(requireContext(), "Invalid Password.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    requireContext(),
                    error.toString().trim { it <= ' ' },
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                val data: MutableMap<String, String> = HashMap()
                data["Content-Type"] = "application/x-www-form-urlencoded"
                data["request"] = "login"
                data["email"] = email
                data["password"] = password
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
        return status
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)



    }

}