package com.example.foodhub.Logged.Admin.Category

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.FoodHubDatabase
import com.example.foodhub.databinding.FragmentViewCategoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ViewCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = ViewCategoryFragment()
    }
    //Mutable List
    private lateinit var categoryList: MutableList<String>

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
        categoryList = mutableListOf<String>()

        lifecycleScope.launch() {
            //Initiate DB
            val db = FoodHubDatabase.getInstance(requireContext())

            //Get category from DB
            categoryList = db.categoryDao.getAllCategoryList() as MutableList<String>

            recycleView = binding.viewCategoryRecycleView
            recycleView.setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            recycleView.layoutManager = layoutManager
            adapter = CategoryListAdapter(requireContext(), categoryList)
            recycleView.adapter = adapter


            //Add Button with Dialog
            addButton = binding.floatingAddButton

            addButton.setOnClickListener() {

                //Add category buttons
                addCategory()
            }
        }

        return binding.root
    }

    //Add category dialog layout
    private fun addCategory() {
        val inflater = LayoutInflater.from(context)
        val v = inflater.inflate(R.layout.add_category_dialog_layout, null)
        val addDialog = AlertDialog.Builder(context)
        val categoryEditText = v.findViewById<EditText>(R.id.categoryAddText)

        addDialog.setView(v)
        addDialog.setPositiveButton("Add"){
                dialog, _ ->
            val categoryName = categoryEditText.text.toString()
            categoryList.add(categoryName)

            //Database Add
            lifecycleScope.launch(){

            }

            //Reset recycle view
            adapter.notifyDataSetChanged()

            //Notify on category added
            Snackbar.make(requireActivity().findViewById(R.id.viewCategoryFragment),"Category Added",
                Snackbar.LENGTH_LONG)
                .setAction("Dismiss"){
                    //Empty to dismiss Snack Bar
                }.show()

            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog, _ ->
            dialog.dismiss()
        }
        addDialog.create()
        addDialog.show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewCategoryViewModel::class.java)
        // TODO: Use the ViewModel


    }
}
