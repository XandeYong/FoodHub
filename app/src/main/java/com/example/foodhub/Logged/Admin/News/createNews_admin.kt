package com.example.foodhub.Logged.Admin.News

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodhub.databinding.FragmentCreateNewsAdminBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class createNews_admin : Fragment() {

    companion object {
        fun newInstance() = createNews_admin()
    }

    private lateinit var viewModel: CreateNewsAdminViewModel
    private lateinit var binding: FragmentCreateNewsAdminBinding

    private val pickImage = 100
    private var imageUri: Uri? = null
    private var imageToString: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNewsAdminBinding.inflate(inflater)
        binding.buttonLoadPicture.setOnClickListener {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, pickImage)

    }
        binding.createnewsBtnAdmin.setOnClickListener{store()}
        return binding.root
    }

    fun store() {
        val preferences = this.requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val test =  preferences.getString("user_name", null)
        binding.textView3.text = test

//      binding.textViewCreateNewsAdminErrorMsg.visibility = View.VISIBLE
        binding.textViewTypeSomethings.error = "Cannot Be Enpty"

//        findNavController().navigate(createNews_adminDirections.actionCreateNewsAdminToNewsListAdmin())
//        Toast.makeText(context, "Successfully created", Toast.LENGTH_LONG).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver , imageUri)
                // initialize byte stream
                val stream = ByteArrayOutputStream()
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                // Initialize byte array
                val bytes: ByteArray = stream.toByteArray()
                // get base64 encoded string
                var encodedString: String = android.util.Base64.encodeToString(bytes, DEFAULT_BUFFER_SIZE)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            binding.imageView.setImageURI(imageUri)
            binding.buttonLoadPicture.alpha = 0.2F

        }

    }
}