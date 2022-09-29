package com.example.foodhub.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.View
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Util {

    //generate timestamp
    fun generateDate(): String {
        val date = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return date.format(formatter).toString()
    }

    //convert image url to bitmap
    suspend fun getBitmap(url: String?, context: Context): Bitmap {
        val loading = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    //convert image to bitmap
//    suspend fun getBitmap(image:)


    //json decoder (Get method)
    fun jsonDecodeGet(view: View?, url: String?): JSONArray {
        var jsonArray: JSONArray = JSONArray()
        var count = 4

        val request: JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                Log.d("response_start", "Response received")
                jsonArray = response.getJSONArray("data")
                count = 10
                //Log.d("response_start", jsonArray.length().toString())
            }, { error ->
                Log.d("response", error.toString())
            }
        )
        val requestQueue = Volley.newRequestQueue(view?.context)
        requestQueue.add(request)

        Log.d("response_start", count.toString())

        return jsonArray
    }
}