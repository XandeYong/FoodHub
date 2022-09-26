package com.example.foodhub.Logged.Donor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.FoodHubDatabase


class DonationFormListAdapter: RecyclerView.Adapter<DonationFormListAdapter.ViewHolder>() {

    //for text data
    private var donationFormIdArray = arrayOf("1","2","3", "1","2","3", "1","2","3", "1","2","3", "1","2","3", "1","2","3")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationFormListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.donation_form_list_card_layout, parent, false)
        return ViewHolder(v)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return donationFormIdArray.size
    }

    override fun onBindViewHolder(holder: DonationFormListAdapter.ViewHolder, position: Int) {
        holder.fieldDonationFormIdDFLCL.text = donationFormIdArray[position]
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var fieldDonationFormIdDFLCL: TextView

        init {
            fieldDonationFormIdDFLCL = itemView.findViewById(R.id.fieldDonationFormIdDFLCL)

            itemView.setOnClickListener(){
               displayDonationFormDetail()

            }
        }

        private fun displayDonationFormDetail(){
            //This need change to navigate to the Donation form Detail
            Toast.makeText(itemView.context, "You clicked on ${donationFormIdArray[absoluteAdapterPosition]}",Toast.LENGTH_LONG).show()
        }
    }

}