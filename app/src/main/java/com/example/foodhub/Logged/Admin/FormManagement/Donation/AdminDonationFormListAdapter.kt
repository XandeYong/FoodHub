package com.example.foodhub.Logged.Admin.FormManagement.Donation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.DonationForm


class AdminDonationFormListAdapter: RecyclerView.Adapter<AdminDonationFormListAdapter.ViewHolder>() {

    var adminDonationFormList: MutableList<DonationForm> = mutableListOf()

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminDonationFormListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.admin_donation_form_list_card_layout, parent, false)
        return ViewHolder(v, mListener)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return adminDonationFormList.size
    }

    override fun onBindViewHolder(holder: AdminDonationFormListAdapter.ViewHolder, position: Int) {
            val adminDonationForm: DonationForm = adminDonationFormList[position]
            holder.fieldDonorIdADFLCL.text = adminDonationForm.accountID
            holder.fieldDonationFormIdADFLCL.text = adminDonationForm.donationFromID
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        lateinit var fieldDonorIdADFLCL: TextView
        lateinit var fieldDonationFormIdADFLCL: TextView

        init {
            fieldDonorIdADFLCL = itemView.findViewById(R.id.fieldDonorIdADFLCL)
            fieldDonationFormIdADFLCL = itemView.findViewById(R.id.fieldDonationFormIdADFLCL)

            itemView.setOnClickListener(){
                listener.onItemClick(adapterPosition)

            }
        }

    }

    fun setData(adfl: List<DonationForm>) {
        adminDonationFormList = adfl as MutableList<DonationForm>
        notifyDataSetChanged()
    }



}