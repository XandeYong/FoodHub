package com.example.foodhub.Logged.Admin.Category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.google.android.material.snackbar.Snackbar

class CategoryListAdapter: RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {
    //Text data
    private var activate: Boolean? = false
    private var categories = arrayOf("Canned Food", "Vegetables", "Beef", "Drinks", "Pork", "Fish", "Egg", "Seafood", "Bread", "Fried", "Packaged Food", "Instant Noodles")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.admin_view_category, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryListAdapter.ViewHolder, position: Int) {
        holder.categoryText.text = categories[position]


        if (activate == true) {
            holder.deleteButton.visibility = View.VISIBLE;
        } else {
            holder.deleteButton.visibility = View.INVISIBLE
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var categoryText: TextView
        lateinit var deleteButton: Button

        init{
            categoryText = itemView.findViewById(R.id.categoryText)
            deleteButton = itemView.findViewById(R.id.deleteButton)

            deleteButton.setOnClickListener(){
                deleteCategoryFunction()
            }
        }
        private fun deleteCategoryFunction(){
            Snackbar.make(itemView,"Deleted [${categories[absoluteAdapterPosition]}]",Snackbar.LENGTH_LONG).
                setAction("Undo"){
                    Snackbar.make(itemView,"Delete reverted!",Snackbar.LENGTH_LONG).show()
                }.show()
        }
    }

    fun deleteToggle(activate: Boolean) {
        this.activate = activate
        notifyDataSetChanged()
    }

}