package com.example.foodhub.Logged.Admin.Category

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.Category
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.util.Util
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CategoryListAdapter(val c:Context, val categoryList:MutableList<String>): RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    val util = Util()
    var categoryPosition: Int = 0

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
                        val v = LayoutInflater.from(c).inflate(R.layout.update_category_dialog_layout, null)
                        val categoryName = v.findViewById<TextInputEditText>(R.id.categoryUpdateText)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Ok"){
                                dialog,_ ->
                                //Update to current Recycler View//
                                //New name
                                var updateCategory = categoryName.text.toString()

                                //Get targeted list position
                                categoryPosition = absoluteAdapterPosition

                                //Reflect update on Recycle View
                                categoryList[categoryPosition] = updateCategory

                                //Initiate DB
                                val db = FoodHubDatabase.getInstance(c)

                                //Get all category class
                                var categoryClass: Category? = null

                                //Split thread to execute Query
                                Thread{
                                    categoryClass = db.categoryDao.getAllCategory()[categoryPosition]

                                    //Update back into DB
                                    db.categoryDao.updateAt(Category(categoryClass!!.categoryID, updateCategory, categoryClass!!.createdAt,util.generateDate()))
                                }.start()

                                //Display category Updated
                                Snackbar.make(v,"Category updated!", Snackbar.LENGTH_LONG)
                                    .setAction("Dismiss"){
                                        //Empty to dismiss Snack Bar
                                    }.show()

                                //Notify Changes
                                notifyDataSetChanged()
                                dialog.dismiss()


                            }
                            .setNegativeButton("Cancel"){
                                dialog,_ ->
                                //Dismiss dialog on Cancel
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }R.id.deleteButton->{
                    //Delete process
                    AlertDialog.Builder(c)
                        .setTitle("Delete")
                        .setIcon(R.drawable.ic_baseline_warning)
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes"){
                            dialog,_ ->
                            //Remove category at Recycler View
                            categoryList.removeAt(absoluteAdapterPosition)

                            //Initiate DB
                            val db = FoodHubDatabase.getInstance(c)

                            //Get all category class
                            var categoryClass: Category? = null

                            Thread{
                                categoryClass = db.categoryDao.getAllCategory()[categoryPosition]

                                //Update back into DB
                                db.categoryDao.deleteAt(categoryClass!!)
                            }.start()

                            Snackbar.make(v,"Category deleted!", Snackbar.LENGTH_LONG)
                                .setAction("Dismiss"){
                                    //Empty to dismiss Snack Bar
                                }.show()

                            //Notify changes
                            notifyDataSetChanged()
                            dialog.dismiss()
                        }
                        .setNegativeButton("No"){
                            dialog,_ ->
                            //Dismiss dialog on Cancel
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                        true
                    }
                    else->true
                }

            }
            //Other necessary functions to enable popup menu
            popupMenu.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu,true)
        }
    }

}