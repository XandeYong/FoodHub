package com.example.foodhub.Logged.Admin.FormManagement.Request

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R

class AdminRequestFormListAdapter: RecyclerView.Adapter<AdminRequestFormListAdapter.ViewHolder>() {
    //for text data
    private var doneeIdArray = arrayOf("D123SS","D5555S","D526SR")
    private var requestFormIdArray = arrayOf("R1","R2","R3")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminRequestFormListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.admin_request_form_list_card_layout, parent, false)
        return ViewHolder(v)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return doneeIdArray.size
    }

    override fun onBindViewHolder(holder: AdminRequestFormListAdapter.ViewHolder, position: Int) {
        holder.field_donee_id_ARFL_CL.text = doneeIdArray[position]
        holder.field_request_form_id_ARFL_CL.text = requestFormIdArray[position]
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var field_donee_id_ARFL_CL: TextView
        lateinit var field_request_form_id_ARFL_CL: TextView

        init {
            field_donee_id_ARFL_CL = itemView.findViewById(R.id.field_donee_id_ARFL_CL)
            field_request_form_id_ARFL_CL = itemView.findViewById(R.id.field_request_form_id_ARFL_CL)

            itemView.setOnClickListener(){
                displayAdminRequestFormDetail()

            }
        }

        private fun displayAdminRequestFormDetail(){
            //This need change to navigate to the Request form Detail
            Toast.makeText(itemView.context, "You clicked on ${doneeIdArray[absoluteAdapterPosition]}",Toast.LENGTH_LONG).show()
        }
    }
}