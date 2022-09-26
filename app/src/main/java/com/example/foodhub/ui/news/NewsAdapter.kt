package com.example.foodhub.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R




class NewsAdapter (): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    //for text data
    private var donorIdArray = arrayOf("WM123SS","WM5555S","WK526SR")
    private var donationFormIdArray = arrayOf("1","2","3")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.one_new_list_item, parent, false)
        return ViewHolder(v)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return donorIdArray.size
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.fieldDonorIdDFLCL.text = donorIdArray[position]
        holder.fieldDonationFormIdDFLCL.text = donationFormIdArray[position]

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var fieldDonorIdDFLCL: TextView
        lateinit var fieldDonationFormIdDFLCL: TextView

        init {
            fieldDonorIdDFLCL = itemView.findViewById(R.id.title_item_text_view)
            fieldDonationFormIdDFLCL = itemView.findViewById(R.id.subtitle_item_text_view)

            itemView.setOnClickListener(){
                displayDonationFormDetail()

            }
        }

        private fun displayDonationFormDetail(){

            //This need change to navigate to the Donation form Detail
            Toast.makeText(itemView.context, "You clicked on ${donorIdArray[absoluteAdapterPosition]}",Toast.LENGTH_LONG).show()
        }
    }

}