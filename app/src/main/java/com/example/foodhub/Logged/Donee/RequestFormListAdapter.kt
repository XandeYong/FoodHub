package com.example.foodhub.Logged.Donee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R

class RequestFormListAdapter: RecyclerView.Adapter<RequestFormListAdapter.ViewHolder>() {
    //for text data
    private var requestFormIdArray = arrayOf("R1","R2","R3")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestFormListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.request_form_list_card_layout, parent, false)
        return ViewHolder(v)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return requestFormIdArray.size
    }

    override fun onBindViewHolder(holder: RequestFormListAdapter.ViewHolder, position: Int) {
        holder.field_request_form_id_RFL_CL.text = requestFormIdArray[position]
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var field_request_form_id_RFL_CL: TextView

        init {
            field_request_form_id_RFL_CL = itemView.findViewById(R.id.field_request_form_id_RFL_CL)

            itemView.setOnClickListener(){
                displayRequestFormDetail()

            }
        }

        private fun displayRequestFormDetail(){
            //This need change to navigate to the Request form Detail
            Toast.makeText(itemView.context, "You clicked on ${requestFormIdArray[absoluteAdapterPosition]}",Toast.LENGTH_LONG).show()
        }
    }
}