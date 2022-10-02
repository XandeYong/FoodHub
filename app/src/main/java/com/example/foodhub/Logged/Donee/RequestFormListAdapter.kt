package com.example.foodhub.Logged.Donee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.RequestForm

class RequestFormListAdapter: RecyclerView.Adapter<RequestFormListAdapter.ViewHolder>() {

    var requestFormList: MutableList<RequestForm> = mutableListOf()

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnClickListener(listener: onItemClickListener){
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestFormListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.request_form_list_card_layout, parent, false)
        return ViewHolder(v, mListener)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return requestFormList.size
    }

    override fun onBindViewHolder(holder: RequestFormListAdapter.ViewHolder, position: Int) {
        holder.field_request_form_id_RFL_CL.text = requestFormList[position].requestFormID
    }

    inner class ViewHolder(itemView: View, listener: RequestFormListAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView){
        lateinit var field_request_form_id_RFL_CL: TextView

        init {
            field_request_form_id_RFL_CL = itemView.findViewById(R.id.field_request_form_id_RFL_CL)

            itemView.setOnClickListener(){
                listener.onItemClick(adapterPosition)
            }
        }
    }

    fun setDataToAdapter(reqFormList: List<RequestForm>) {
        requestFormList = reqFormList as MutableList<RequestForm>
        notifyDataSetChanged()
    }


}