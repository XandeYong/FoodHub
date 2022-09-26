package com.example.foodhub.Logged.Admin.FormManagement.Donation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R

class AdminDonationFormListAdapter: RecyclerView.Adapter<AdminDonationFormListAdapter.ViewHolder>() {
    //for text data
    private var donorIdArray = arrayOf("WM123SS","WM5555S","WK526SR","WK526SR","WK56SR","WK26SR","W526SR","WK526S","WKSR","WK526SR","WK526SR","WK526SR","WK526SR","WK526SR","WK526SR","WK526SR","WK526SR","WK526SR")
    private var donationFormIdArray = arrayOf("1","2","3", "1","2","3", "1","2","3", "1","2","3", "1","2","3", "1","2","3")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDonationFormListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.admin_donation_form_list_card_layout, parent, false)
        return ViewHolder(v)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return donorIdArray.size
    }

    override fun onBindViewHolder(holder: AdminDonationFormListAdapter.ViewHolder, position: Int) {
        holder.fieldDonorIdADFLCL.text = donorIdArray[position]
        holder.fieldDonationFormIdADFLCL.text = donationFormIdArray[position]
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var fieldDonorIdADFLCL: TextView
        lateinit var fieldDonationFormIdADFLCL: TextView

        init {
            fieldDonorIdADFLCL = itemView.findViewById(R.id.fieldDonorIdADFLCL)
            fieldDonationFormIdADFLCL = itemView.findViewById(R.id.fieldDonationFormIdADFLCL)

            itemView.setOnClickListener(){
                displayAdminDonationFormDetail()

            }
        }

        private fun displayAdminDonationFormDetail(){
            //This need change to navigate to the Donation form Detail
            Toast.makeText(itemView.context, "You clicked on ${donorIdArray[absoluteAdapterPosition]}",Toast.LENGTH_LONG).show()
        }
    }
}