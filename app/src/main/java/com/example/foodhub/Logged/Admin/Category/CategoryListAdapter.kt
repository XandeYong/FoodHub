package com.example.foodhub.Logged.Admin.Category

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
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.R
import com.example.foodhub.database.Category
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.util.Util
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

class CategoryListAdapter(val c:Context, val categoryList:MutableList<String>): RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    val util = Util()

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
                when(it.itemId) {
                    R.id.editButton -> {
                        val v = LayoutInflater.from(c).inflate(R.layout.update_category_dialog_layout, null)
                        val categoryName = v.findViewById<TextInputEditText>(R.id.categoryUpdateText)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Ok"){
                                dialog,_ ->
                                //Update to current Recycler View//
                                //New name
                                var updateCategory = categoryName.text.toString()

                                //Initiate DB
                                val db = FoodHubDatabase.getInstance(c)

                                //Get all category class
                                var categoryClass: Category? = null

                                //Split thread to execute Query
                                Thread{
                                    categoryClass = db.categoryDao.getAllCategory()[absoluteAdapterPosition]

                                    //Update back into DB
                                    db.categoryDao.updateAt(Category(categoryClass!!.categoryID, updateCategory, categoryClass!!.createdAt,util.generateDate()))

                                    //Update in remote DB
                                    updateCategoryInRemoteDB(categoryClass!!.categoryID, updateCategory)

                                    //Log
                                    Log.i("Category", "Category added to DB")
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
                                categoryClass = db.categoryDao.getAllCategory()[absoluteAdapterPosition]

                                //Delete from DB
                                db.categoryDao.deleteAt(categoryClass!!)

                                //Delete from Remote DB
                                Log.i("cateClass", categoryClass!!.categoryID)
                                deleteCategoryInRemoteDB(v.context, categoryClass!!.categoryID)

                                //Log
                                Log.i("Category", "Category added to DB")
                            }.start()

                            //Display Snack bar
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

        //Process update in Remote DB
        private fun updateCategoryInRemoteDB(catID: String, catName: String) {

            //URL String
            var url = "http://10.0.2.2/foodhub_server/category.php"

            val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->

                    val jsonResponse = JSONObject(response)
                    val status = jsonResponse.getInt("status")
                    val message = jsonResponse.getString("message")


                    if (status == 0) {
                        Log.i("RemoteRequest", message)
                    }else {
                        Log.i("RemoteRequest", message)
                    }

                },
                Response.ErrorListener { error ->
                    Log.i("RemoteError",error.toString().trim { it <= ' ' })
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>? {

                    val data: MutableMap<String, String> = HashMap()
                    data["Content-Type"] = "application/x-www-form-urlencoded"
                    data["request"] = "UpdateCategory"
                    data["categoryID"] = catID
                    data["categoryName"] = catName

                    return data
                }
            }
            val requestQueue = Volley.newRequestQueue(c)
            requestQueue.add(stringRequest)

        }

        //Process delete in Remote DB
        private fun deleteCategoryInRemoteDB(context: Context, catID: String) {

            //URL String
            var url = "http://10.0.2.2/foodhub_server/category.php"

            val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { response ->

                val jsonResponse = JSONObject(response)
                val status = jsonResponse.getInt("status")
                val message = jsonResponse.getString("message")



                if (status == 0) {
                    Log.i("RemoteRequest", message)
                } else if(status == -3) {
                    Toast.makeText(context, "Unable to delete from remote db, This category has been linked by one or more form", Toast.LENGTH_SHORT).show()
                }else {
                    Log.i("RemoteRequest", message)
                }

            },
                Response.ErrorListener { error ->
                    Log.i("RemoteError",error.toString().trim { it <= ' ' })
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>? {

                    val data: MutableMap<String, String> = HashMap()
                    data["Content-Type"] = "application/x-www-form-urlencoded"
                    data["request"] = "DeleteCategory"
                    data["categoryID"] = catID

                    return data
                }
            }
            val requestQueue = Volley.newRequestQueue(c)
            requestQueue.add(stringRequest)

        }

    }

}