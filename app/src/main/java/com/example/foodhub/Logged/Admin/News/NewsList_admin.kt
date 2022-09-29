package com.example.foodhub.Logged.Admin.News


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var viewModel: NewsListAdminViewModel

    private lateinit var adapter: NewsListAdminAdapterTest
    private lateinit var newList : ArrayList<NewsListAdminViewModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewsListAdminBinding.inflate(inflater)

        binding.floatingBtnNewsListBtn.setOnClickListener{createNews_admin(it)}

        return binding.root
    }

    fun createNews_admin(view: View){
        findNavController().navigate(NewsList_adminDirections.actionNewsListAdminToCreateNewsAdmin())

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewsListAdminViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.news.observe(viewLifecycleOwner,Observer{ it ->
            recyclerView = binding.recyclerViewNewsListAdmin
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager  = LinearLayoutManager(this.requireContext())
            adapter = NewsListAdminAdapterTest(it)
            recyclerView.adapter = adapter

            adapter.onItemClick = {
                val preferences = this.requireActivity().getSharedPreferences("pass", Context.MODE_PRIVATE)
                val editor =preferences.edit()

                editor.putString("id",it.newsID.toString())
                editor.putString("tittle" , it.title.toString())
                editor.apply()
                editor.commit()

                findNavController().navigate(NewsList_adminDirections.actionNewsListAdminToUpdateNewsAdmin())
            }
        })
    }

}