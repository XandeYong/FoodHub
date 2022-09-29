package com.example.foodhub.Logged.Admin.FormManagement.Request

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.RequestForm

class AdminRequestFormListAdapter: RecyclerView.Adapter<AdminRequestFormListAdapter.ViewHolder>() {

    var adminRequestFormList: MutableList<RequestForm> = mutableListOf()

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminRequestFormListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.admin_request_form_list_card_layout, parent, false)
        return ViewHolder(v, mListener)
    }

    //Identify how many item are passing to the view holder
    override fun getItemCount(): Int {
        return adminRequestFormList.size
    }

    override fun onBindViewHolder(holder: AdminRequestFormListAdapter.ViewHolder, position: Int) {
        val adminRequestForm: RequestForm = adminRequestFormList[position]
        holder.field_donee_id_ARFL_CL.text = adminRequestForm.accountID
        holder.field_request_form_id_ARFL_CL.text = adminRequestForm.requestFormID
    }

    inner class ViewHolder(itemView: View, listener: AdminRequestFormListAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView){
        lateinit var field_donee_id_ARFL_CL: TextView
        lateinit var field_request_form_id_ARFL_CL: TextView

        init {
            field_donee_id_ARFL_CL = itemView.findViewById(R.id.field_donee_id_ARFL_CL)
            field_request_form_id_ARFL_CL = itemView.findViewById(R.id.field_request_form_id_ARFL_CL)

            itemView.setOnClickListener(){
                listener.onItemClick(adapterPosition)
            }
        }
    }

    fun setData(arfl: List<RequestForm>) {
        adminRequestFormList = arfl as MutableList<RequestForm>
        notifyDataSetChanged()
    }

}