package com.example.foodhub.util

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class UploadImageClass(activity: Activity) {

    var activity = activity;
    var dialog: ProgressDialog? = null
    var serverURL: String = "http://10.0.2.2/foodhub_server/uploadNewImage.php"
    var serverUploadDirectoryPath: String = "http://10.0.2.2/foodhub_server/image/news/"
    val client = OkHttpClient()


    fun uploadFile(sourceFilePath: String, uploadedFileName: String? = null): String {
        var returnPath = uploadFile(File(sourceFilePath), uploadedFileName)
        return returnPath
    }

    fun uploadFile(sourceFileUri: Uri, uploadedFileName: String? = null): String {
        val pathFromUri = URIPathHelper().getPath(activity,sourceFileUri)
        var returnPath = uploadFile(File(pathFromUri), uploadedFileName)
        return returnPath
    }

    fun uploadFile(sourceFile: File, uploadedFileName: String? = null): String {
        var returnPath : String = ""
        Thread {
            val mimeType = getMimeType(sourceFile);
            if (mimeType == null) {
                Log.e("file error", "Not able to get mime type")
                return@Thread
            }
            val fileName: String = if (uploadedFileName == null)  sourceFile.name else uploadedFileName

                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("uploaded_file", fileName,sourceFile.asRequestBody(mimeType.toMediaTypeOrNull()))
                        .build()

                val request: Request = Request.Builder().url(serverURL).post(requestBody).build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    Log.d("File upload","success, path: $serverUploadDirectoryPath$fileName")
                    showToast("File uploaded successfully at $serverUploadDirectoryPath$fileName")
                    returnPath = "$serverUploadDirectoryPath$fileName"

                } else {
                    Log.e("File upload", "failed")
                    showToast("File uploading failed")
                }

            toggleProgressDialog(false)
        }.start()
        return returnPath
    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? =  "image/jpeg"
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }



    fun showToast(message: String) {
        activity.runOnUiThread {
            Toast.makeText( activity, message, Toast.LENGTH_LONG ).show()
        }
    }

    fun toggleProgressDialog(show: Boolean) {
        activity.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(activity, "", "Uploading file...", true);
            } else {
                dialog?.dismiss();
            }
        }
    }

}