package com.example.foodhub.Logged.Admin.News

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.FragmentCreateNewsAdminBinding
import com.example.foodhub.ui.register.RegisterFragmentDirections
import com.example.foodhub.util.URIPathHelper
import com.example.foodhub.util.UploadImageClass
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URLConnection


class createNews_admin : Fragment() {

    companion object {
        fun newInstance() = createNews_admin()
    }

    private val URL: String = "http://10.0.2.2/foodhub_server/news.php"
    private lateinit var viewModel: CreateNewsAdminViewModel
    private lateinit var binding: FragmentCreateNewsAdminBinding

    private val pickImage = 100
    private var imageUri: Uri? = null
    private var imageToString: String? = null

    var objectStatus : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNewsAdminBinding.inflate(inflater)
        binding.buttonLoadPicture.setOnClickListener {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)

    }
        binding.createnewsBtnAdmin.setOnClickListener{
            if(!binding.textViewTypeSomethings.text.toString().isNullOrEmpty() && imageUri.toString() != "null"
                && !binding.txtWebsiteUrl.text.toString().isNullOrEmpty()){
                if(!binding.txtWebsiteUrl.text.toString().startsWith("http")){
                binding.txtWebsiteUrl.setError("Website URL format not correct")
            }else {
                    store()
            }

            }else {
                Log.i("CheckImage" , imageUri.toString())
                if(binding.textViewTypeSomethings.text.toString().isNullOrEmpty()){
                    binding.textViewTypeSomethings.setError("Cannot Be Empty")
                }
                if(imageUri.toString() == "null"){
                    binding.textView3.setError("Image Cannot Be Empty")
                }
                if(binding.txtWebsiteUrl.text.toString().isNullOrEmpty()){
                    binding.txtWebsiteUrl.setError("Website URL Cannot Be Empty")
                }

            }

        }
        return binding.root
    }

    fun store() {
        Log.i("YES123" , "did i enter?")
        var text = binding.textViewTypeSomethings.text.toString()
        var url = binding.txtWebsiteUrl.text.toString()
        var bitmap = (binding.imageView.drawable as BitmapDrawable).bitmap

        var fileName :String = ""

        lifecycleScope.launch{
            val db = FoodHubDatabase.getInstance(requireContext())
            var allnews = db.newsDao.getLatest()
            var id = generateNewsId(allnews.newsID)
            fileName  = id
            Log.i("YES123" ,fileName)
            db.newsDao.createSpecificNews(id,text,bitmap,url)
            createToRemoteNews(id,text,url)
            UploadImageClass(requireActivity()).uploadFile(imageUri!!,fileName)

            if (objectStatus == 0) {
                Toast.makeText(requireContext(), "Insert Successfully", Toast.LENGTH_SHORT).show()

            }else {
                Toast.makeText(requireContext(), "There was Something Wrong!", Toast.LENGTH_SHORT).show()
            }

            findNavController().navigate(createNews_adminDirections.actionCreateNewsAdminToNewsListAdminFragment())
        }

    }

    fun createToRemoteNews(id: String, title:String, url: String){
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, URL,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                objectStatus = jsonResponse.getInt("status")

            },
            Response.ErrorListener { error ->
                Log.d("ErrorInExceptiom" ,error.toString())
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {

                val data: MutableMap<String, String> = HashMap()
                data["Content-Type"] = "application/x-www-form-urlencoded"
                data["request"] = "createNews"
                data["id"] = id
                data["title"] = title
                data["url"] = url
                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    fun generateNewsId(id:String): String {
        var newsID: String = "N1"
        if(id != null) {
            val value: Int=  id.substring(1).toInt() + 1
            newsID = "N" + value.toString()
        }
        return newsID
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.imageView.setImageURI(imageUri)
            binding.buttonLoadPicture.alpha = 0.2F
            binding.textView3.setError(null)

        }

    }

    fun isImageFile(path: String?): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("image")
    }
}