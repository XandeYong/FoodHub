package com.example.foodhub.Logged.Admin.Category

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.foodhub.R
import com.example.foodhub.database.Category
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.FragmentViewCategoryBinding
import com.example.foodhub.util.Util
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.json.JSONObject

class ViewCategoryFragment : Fragment() {

    val util = Util()

    companion object {
        fun newInstance() = ViewCategoryFragment()
    }
    //Mutable List
    private lateinit var categoryList: MutableList<String>

    //Static Mutable List
    private lateinit var staticList: MutableList<String>

    //Add Components
    private lateinit var addButton: FloatingActionButton

    //Add Recycle View
    private lateinit var recycleView: RecyclerView
    private lateinit var binding: FragmentViewCategoryBinding
    private lateinit var viewModel: ViewCategoryViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: CategoryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewCategoryBinding.inflate(inflater)

        //Initialized List
        categoryList = mutableListOf()

        lifecycleScope.launch() {
            //Initiate DB
            val db = FoodHubDatabase.getInstance(requireContext())

            //Get category from DB
            categoryList = db.categoryDao.getAllCategoryList() as MutableList<String>

            //Pass to static list
            staticList = categoryList

            recycleView = binding.viewCategoryRecycleView
            recycleView.setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            recycleView.layoutManager = layoutManager
            adapter = CategoryListAdapter(requireContext(), categoryList)
            recycleView.adapter = adapter

            //Add Button with Dialog
            addButton = binding.floatingAddButton

            addButton.setOnClickListener() {

                lifecycleScope.launch(){
                    //Add category buttons
                    addCategory(db.categoryDao.getLatest())
                }

            }

            binding.searchText.addTextChangedListener(object: TextWatcher{
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        if(s.isEmpty()){
                            adapter = CategoryListAdapter(requireContext(), staticList)
                            recycleView.adapter = adapter
                        }
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //TODO("Not yet implemented")
                }

                override fun afterTextChanged(s: Editable?) {
                    //TODO("Not yet implemented")
                }
            })

            //Search function
            binding.searchButton.setOnClickListener(){
                //Remove Keyboard
                view?.let { activity?.hideKeyboard(it) }

                //search()
                var searchedInfo = mutableListOf<String>()

                lifecycleScope.launch(){
                    val db = FoodHubDatabase.getInstance(requireContext())
                    val searchName = binding.searchText.text.toString()

                    searchedInfo = db.categoryDao.searchCategory("%$searchName%") as MutableList<String>

                    if(searchedInfo.size > 0){
                        categoryList = searchedInfo
                        adapter = CategoryListAdapter(requireContext(), categoryList)
                        recycleView.adapter = adapter
                        Toast.makeText(requireContext(), "Donation List Found!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(), "Donation List Not Found!", Toast.LENGTH_SHORT).show()
                    }

                }

            }
        }

        return binding.root
    }

    //Add category dialog layout
    private fun addCategory(latestCategory: Category) {
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.add_category_dialog_layout, null)
        val addDialog = AlertDialog.Builder(context)
        val categoryEditText = v.findViewById<EditText>(R.id.categoryAddText)

        addDialog.setView(v)
        addDialog.setPositiveButton("Add"){
                dialog, _ ->
            //Get text from Add Text
            val categoryName = categoryEditText.text.toString()

            //Adding new category to DB process//
            //New Id
            var latestId = latestCategory.categoryID
            var result = latestId.filter{it.isDigit()}

            latestId = "C" + (result.toInt()+ 1)

            //Add to MutableList
            categoryList.add(0,categoryName)

            //Database Add Locale
            lifecycleScope.launch() {
                //Adding to DB//
                //Initiate DB
                val db = FoodHubDatabase.getInstance(requireContext())

                //Creating new category class
                var newCategory = Category(latestId,categoryName,util.generateDate(), util.generateDate())

                //Insert to DB
                db.categoryDao.insert(newCategory)

                //Reset recycle view
                adapter.notifyDataSetChanged()

                //Add into remote DB
                addCategoryInRemoteDB(latestId, categoryName)

                //Log
                Log.i("Category", "Category added to DB")

                //Notify on category added
                Snackbar.make(
                    requireActivity().findViewById(R.id.viewCategoryFragment), "Category Added",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Dismiss") {
                        //Empty to dismiss Snack Bar
                    }.show()

                dialog.dismiss()
            }


        }
        //Dismiss button
        addDialog.setNegativeButton("Cancel"){
                dialog, _ ->
            dialog.dismiss()
        }
        //Show Dialog box
        addDialog.create()
        addDialog.show()
    }

    //Hide keyboard function
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //Process add into Remote DB
    private fun addCategoryInRemoteDB(catID: String, catName: String) {

        //URL String
        var url = "http://10.0.2.2/foodhub_server/category.php"

        val stringRequest: StringRequest = object : StringRequest(
            Method.POST, url, Response.Listener { response ->

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
                data["request"] = "AddCategory"
                data["categoryID"] = catID
                data["categoryName"] = catName

                return data
            }
        }
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewCategoryViewModel::class.java)
        //TODO: Use the ViewModel
    }
}
