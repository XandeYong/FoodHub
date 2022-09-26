package com.example.foodhub.Logged.Admin.Category

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.databinding.FragmentViewCategoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial

class ViewCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = ViewCategoryFragment()
    }

    //Delete Components
    private lateinit var deleteModeSwitch: SwitchMaterial

    //Add Components
    private lateinit var addButton: FloatingActionButton

    //Add Recycle View
    private lateinit var recycleView: RecyclerView
    private lateinit var binding: FragmentViewCategoryBinding
    private lateinit var viewModel: ViewCategoryViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: RecyclerView.Adapter<CategoryListAdapter.ViewHolder>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewCategoryBinding.inflate(inflater)

        //Add to RecycleView
        recycleView = binding.viewCategoryRecycleView
        recycleView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        recycleView.layoutManager = layoutManager
        adapter = CategoryListAdapter()
        recycleView.adapter = adapter

        //Switch Control
        deleteModeSwitch = binding.deleteModeSwitch
        deleteModeSwitch.setOnCheckedChangeListener{ _, _ ->

            if(deleteModeSwitch.isChecked){

                (adapter as CategoryListAdapter).deleteToggle(true)
            }else{
                (adapter as CategoryListAdapter).deleteToggle(false)
            }

        }

        //Add Button with Dialog
        addButton = binding.floatingAddButton
        val builder = AlertDialog.Builder(context)


        addButton.setOnClickListener(){
            var dialog = AddDialogFragment()

            dialog.show(childFragmentManager, "addCategoryDialog")
        }




        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewCategoryViewModel::class.java)
        // TODO: Use the ViewModel


    }






}