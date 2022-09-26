package com.example.foodhub.Logged.Admin.News


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.databinding.FragmentNewsListAdminBinding


class NewsList_admin : Fragment() {

    companion object {
        fun newInstance() = NewsList_admin()
    }

    private lateinit var binding : FragmentNewsListAdminBinding
    private lateinit var recyclerView: RecyclerView



    private lateinit var adapter: NewsListAdminAdapterTest
    private lateinit var newList : ArrayList<NewsListAdminViewModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewsListAdminBinding.inflate(inflater)

        // Add the following lines to create RecyclerView
        recyclerView = binding.recyclerViewNewsListAdmin
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager  = LinearLayoutManager(this.requireContext())

        newList= ArrayList()

        newList.add(NewsListAdminViewModel(1,  "testl"))
        newList.add(NewsListAdminViewModel(2,  "testl"))
        newList.add(NewsListAdminViewModel(3,  "testl"))
        newList.add(NewsListAdminViewModel(4,  "testl"))
        newList.add(NewsListAdminViewModel(5, "testl"))
        newList.add(NewsListAdminViewModel(6, "testl"))


        adapter = NewsListAdminAdapterTest(newList)
        recyclerView.adapter = adapter


        adapter.onItemClick = {
            val preferences = this.requireActivity().getSharedPreferences("pass", Context.MODE_PRIVATE)
            val editor =preferences.edit()

            editor.putString("id",it.id.toString())
            editor.putString("tittle" , it.title.toString())
            editor.apply()
            editor.commit()

            findNavController().navigate(NewsList_adminDirections.actionNewsListAdminToUpdateNewsAdmin())
        }

        binding.floatingBtnNewsListBtn.setOnClickListener{createNews_admin(it)}

        return binding.root
    }

    fun createNews_admin(view: View){
        findNavController().navigate(NewsList_adminDirections.actionNewsListAdminToCreateNewsAdmin())

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(NewsListAdminViewModel::class.java)
        // TODO: Use the ViewModel
    }

}