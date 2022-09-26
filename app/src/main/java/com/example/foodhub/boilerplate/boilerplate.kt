package com.example.foodhub.boilerplate

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.foodhub.R
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.database.State
import com.example.foodhub.ui.news.NewsFragment
import com.example.foodhub.util.Util
import kotlinx.coroutines.launch

class boilerplate: Fragment() {

    val util = Util()

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_news, container, false)
        lifecycleScope.launch {
            util.getBitmap("https://cdn.icon-icons.com/icons2/1378/PNG/512/avatardefault_92824.png", requireContext())
        }

        return view
    }

    //interact with room database
    private suspend fun databaseInteraction() {
        var db = FoodHubDatabase.getInstance(requireContext())
        var state = State()
        lifecycleScope.launch {
            state = db.stateDao.getLatest()
        }
    }

}