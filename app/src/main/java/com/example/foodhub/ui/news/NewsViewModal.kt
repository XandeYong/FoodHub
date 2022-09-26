package com.example.foodhub.ui.news

import android.content.Context
import android.os.Parcelable
import com.example.foodhub.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsViewModal(
    val id: Int,
    val banner: Int,
    val title: String,

) : Parcelable {

    companion object {
        fun sportsList(context: Context): List<NewsViewModal> {
            return listOf(
                NewsViewModal(1, R.drawable.image1,
                    context.getString(R.string.title1)
                ),
                NewsViewModal(2, R.drawable.image2,
                    context.getString(R.string.title2)
                ),
                NewsViewModal(3, R.drawable.image3,
                    context.getString(R.string.title3)
                ),
                NewsViewModal(4, R.drawable.image4,
                    context.getString(R.string.title4)
                ),
                NewsViewModal(5, R.drawable.image5,
                    context.getString(R.string.title5)
                ),
                NewsViewModal(6, R.drawable.image6,
                    context.getString(R.string.title7)
                )
            )
        }
    }
}