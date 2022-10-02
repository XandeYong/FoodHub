package com.example.foodhub.Logged.Admin.News

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.FragmentUpdateNewsAdminBinding
import com.example.foodhub.util.URIPathHelper
import com.example.foodhub.util.UploadImageClass
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.net.URLConnection

class updateNews_admin : Fragment() {

    companion object {
        fun newInstance() = updateNews_admin()
    }

    private val URL: String = "http://10.0.2.2/foodhub_server/news.php"
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var imageToString: String? = null

    private lateinit var viewModel: UpdateNewsAdminViewModel
    private lateinit var binding: FragmentUpdateNewsAdminBinding
    private lateinit var id: String
    private lateinit var url: String

    private var action:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateNewsAdminBinding.inflate(inflater)

        val preferences = this.requireActivity().getSharedPreferences("pass", Context.MODE_PRIVATE)
        id = preferences.getString("id", null).toString()
        preferences.edit().remove("id").commit()

        binding.btnUpdate.setOnClickListener {
            action = 0
            if(!binding.txtUpdateSomethings.text.toString().isNullOrEmpty()  && !imageUri.toString().isNullOrEmpty()
                && !binding.txtWebsiteUrl.text.toString().isNullOrEmpty() ){
                confirmDialogBox(action)
            }else {
                Log.i("Why123",imageUri.toString())
                if(binding.txtUpdateSomethings.text.toString().isNullOrEmpty()){
                    binding.txtUpdateSomethings.setError("Cannot Be Empty")
                }
                if(imageUri.toString().isNullOrEmpty()){

                    binding.textView3.setError("Image Cannot Be Empty")
                }
                if(binding.txtWebsiteUrl.text.toString().isNullOrEmpty()){
                    binding.textView3.setError("Website URL Cannot Be Empty")
                }
            }

        }
        binding.btnDelete.setOnClickListener{
            action =1
            confirmDialogBox(action)
        }

        binding.btnUpdateImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        return binding.root
    }

    fun updateNews() {
        var bitmap = (binding.updateImageView.drawable as BitmapDrawable).bitmap
        var text = binding.txtUpdateSomethings.text.toString()
        var url = binding.txtWebsiteUrl.text.toString()

        val pathFromUri = URIPathHelper().getPath(requireActivity(),imageUri!!)


        if( isImageFile(pathFromUri)){
            lifecycleScope.launch{
                val db = FoodHubDatabase.getInstance(requireActivity())
                // need get database and generate url

                db.newsDao.updateNews(text,bitmap,url,id)
                updateToRemoteNews(id, text , url)
            }
            var fileName :String = ""
            fileName  = id
            UploadImageClass(requireActivity()).uploadFile(imageUri!!,fileName)
        }else {
            binding.textView3.setError("Is not image file")
        }




    }
    fun deleteNews(id: String){
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener { response ->

                val jsonResponse = JSONObject(response)
                var objectStatus = jsonResponse.getInt("status")

//                if (objectStatus == 0) {
//                    Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMainFragment())
//                }else {
//                    Toast.makeText(context, "There was Something Wrong!", Toast.LENGTH_SHORT).show()
//                }
            },
            Response.ErrorListener { error ->
                Log.d("ErrorInExceptiom" ,error.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                val data: MutableMap<String, String> = HashMap()
                data["Content-Type"] = "application/x-www-form-urlencoded"
                data["request"] = "deleteNews"
                data["id"] = id
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    fun updateToRemoteNews(id: String,title:String ,url:String){
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener { response ->

                val jsonResponse = JSONObject(response)
                var objectStatus = jsonResponse.getInt("status")

//                if (objectStatus == 0) {
//                    Toast.makeText(context, "Update Successfully", Toast.LENGTH_SHORT).show()
//                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMainFragment())
//                }else {
//                    Toast.makeText(context, "There was Something Wrong!", Toast.LENGTH_SHORT).show()
//                }
            },
            Response.ErrorListener { error ->
                Log.d("ErrorInExceptiom" ,error.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                val data: MutableMap<String, String> = HashMap()
                data["Content-Type"] = "application/x-www-form-urlencoded"
                data["request"] = "updateNews"
                data["id"] = id
                data["title"] = title
                data["url"] = url
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    fun deleteNews() {
        var bitmap = (binding.updateImageView.drawable as BitmapDrawable).bitmap
        var text = binding.txtUpdateSomethings.text.toString()
        lifecycleScope.launch{
            val db = FoodHubDatabase.getInstance(requireActivity())
            db.newsDao.clearSpecificNews(id)
            // need get database and generate url
            deleteNews(id)
        }
    }

    fun confirmDialogBox(actionNum :Int) {
        var actionString:String = ""
        var buidle = AlertDialog.Builder(context)
        if(actionNum == 0){
             actionString = "update"

            buidle.setTitle("Confirm Update")
            buidle.setMessage("Do you want to update this news ?")
            buidle.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                updateNews()
                Toast.makeText(requireContext(), "Update Successfully", Toast.LENGTH_LONG).show()
                dialog.cancel()
                findNavController().navigate(updateNews_adminDirections.actionUpdateNewsAdminToNewsListAdminFragment())
            })
            buidle.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->

                Toast.makeText(requireContext(), "No Change", Toast.LENGTH_LONG).show()
                dialog.cancel()

            })
        }else {

            buidle.setTitle("Confirm Delete")
            buidle.setMessage("Do you want to delete this news ?")
            buidle.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->

                deleteNews()
                Toast.makeText(requireContext(), "Delete Successfully", Toast.LENGTH_LONG).show()
                dialog.cancel()
                findNavController().navigate(updateNews_adminDirections.actionUpdateNewsAdminToNewsListAdminFragment())
                // need navigate back to the receiver
            })
            buidle.setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->

                Toast.makeText(requireContext(), "No Change", Toast.LENGTH_LONG).show()
                dialog.cancel()

            })
        }
        var alert = buidle.create()
        alert.show()
        // need navigate back to the receiver
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateNewsAdminViewModel::class.java)
        // TODO: Use the ViewModel
        lifecycleScope.launch {
            viewModel.getSpecificNewData(requireContext(), id.toString())
            binding.txtUpdateSomethings.setText(viewModel.news.title.toString())
            binding.updateImageView.load(viewModel.news.image)
            binding.btnUpdateImage.alpha = 0.2F
            url = viewModel.news.url.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.updateImageView.setImageURI(imageUri)
            binding.textView3.setError(null)
        }

    }

    fun isImageFile(path: String?): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("image")
    }
}