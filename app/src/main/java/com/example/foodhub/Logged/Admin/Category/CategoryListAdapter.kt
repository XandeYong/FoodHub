package com.example.foodhub.Logged.Admin.Category

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CategoryListAdapter(val c:Context, val categoryList:MutableList<String>): RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.admin_view_category, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryListAdapter.ViewHolder, position: Int) {

        holder.categoryText.text = categoryList[position]
    }

    //This
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var categoryText: TextView
        private var smallMenu: ImageView

        init{
            //Views
            categoryText = itemView.findViewById(R.id.categoryText)
            smallMenu = itemView.findViewById(R.id.smallMenu)
            smallMenu.setOnClickListener(){
                popupMenu(it)
            }

        }

        private fun popupMenu(v: View) {
            val popupMenu = PopupMenu(c,v)
            popupMenu.inflate(R.menu.show_category_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editButton->{
                        val v = LayoutInflater.from(c).inflate(R.layout.add_category_dialog_layout, null)
                        val categoryName = v.findViewById<TextInputEditText>(R.id.categoryAddText)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Ok"){
                                dialog,_ ->
                                categoryList[absoluteAdapterPosition] = categoryName.text.toString()
                                Snackbar.make(v,"Category updated!", Snackbar.LENGTH_LONG)
                                    .setAction("Dismiss"){
                                        //Empty to dismiss Snack Bar
                                    }.show()
                                notifyDataSetChanged()
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancel"){
                                dialog,_ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }R.id.deleteButton->{
                    //Delete Code Here
                    AlertDialog.Builder(c)
                        .setTitle("Delete")
                        .setIcon(R.drawable.ic_baseline_warning)
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes"){
                            dialog,_ ->
                            categoryList.removeAt(absoluteAdapterPosition)
                            notifyDataSetChanged()
                            dialog.dismiss()
                        }
                        .setNegativeButton("No"){
                            dialog,_ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                        true
                    }
                    else->true
                }

            }
            popupMenu.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu,true)
        }
    }

}