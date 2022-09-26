package com.example.foodhub.Logged.Admin.News

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R



class NewsListAdapter_admin: RecyclerView.Adapter<NewsListAdapter_admin.ViewHolder>() {

    //for text data
    private var donorIdArray = arrayOf("WM123SS12","WM5555S","WK526SR")
    private var donationFormIdArray = arrayOf("1","2","3")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListAdapter_admin.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.one_news_item_list_admin, parent, false)
        return ViewHolder(v)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return donorIdArray.size
    }

    override fun onBindViewHolder(holder: NewsListAdapter_admin.ViewHolder, position: Int) {
        holder.fieldDonorIdDFLCL.text = donorIdArray[position]
        holder.fieldDonationFormIdDFLCL.text = donationFormIdArray[position]
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var fieldDonorIdDFLCL: TextView
        lateinit var fieldDonationFormIdDFLCL: TextView

        init {
            fieldDonorIdDFLCL = itemView.findViewById(R.id.newsList_admin_title)
            fieldDonationFormIdDFLCL = itemView.findViewById(R.id.newsList_admin_subtitle)

            itemView.setOnClickListener(){
                displayDonationFormDetail()

            }
        }

         private fun displayDonationFormDetail() {

            Toast.makeText(itemView.context, "You clicked on ${donorIdArray[absoluteAdapterPosition]}",Toast.LENGTH_LONG).show()
        }
    }

}