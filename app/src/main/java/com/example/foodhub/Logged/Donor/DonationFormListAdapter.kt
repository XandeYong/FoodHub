package com.example.foodhub.Logged.Donor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.DonationForm


class DonationFormListAdapter: RecyclerView.Adapter<DonationFormListAdapter.ViewHolder>() {

    var donationFormList: MutableList<DonationForm> = mutableListOf()

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationFormListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.donation_form_list_card_layout, parent, false)
        return ViewHolder(v, mListener)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return donationFormList.size
    }

    override fun onBindViewHolder(holder: DonationFormListAdapter.ViewHolder, position: Int) {
        val donationForm: DonationForm = donationFormList[position]
        holder.fieldDonationFormIdDFLCL.text = donationForm.donationFormID
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        lateinit var fieldDonationFormIdDFLCL: TextView

        init {
            fieldDonationFormIdDFLCL = itemView.findViewById(R.id.fieldDonationFormIdDFLCL)

            itemView.setOnClickListener(){
                listener.onItemClick(adapterPosition)
            }
        }

    }

    fun setData(dfl: List<DonationForm>) {
        donationFormList = dfl as MutableList<DonationForm>
        notifyDataSetChanged()
    }


}