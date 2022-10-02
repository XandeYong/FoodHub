package com.example.foodhub.Logged.Admin.News


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.databinding.FragmentNewsListAdminBinding
import kotlinx.coroutines.launch


class NewsListAdminFragment : Fragment() {

    companion object {
        fun newInstance() = NewsListAdminFragment()
    }

    private lateinit var binding : FragmentNewsListAdminBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: NewsListAdminViewModel

    private lateinit var adapter: NewsListAdapterForAdmin
    private lateinit var newList : ArrayList<NewsListAdminViewModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewsListAdminBinding.inflate(inflater)

        binding.createNewsBtn.setOnClickListener{
            findNavController().navigate(NewsListAdminFragmentDirections.actionNewsListAdminFragmentToCreateNewsAdmin())
        }
        binding.btnSearchNews.setOnClickListener() {
            searchReqFormID()
        }

        binding.btnSearchNews.setOnClickListener{
            searchReqFormID()
        }

        binding.editSearchNews.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if(s.isEmpty()){
                     viewModel.news.observe(viewLifecycleOwner,Observer{
                         adapter.setDataToAdapter(it)
                     })
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
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewsListAdminViewModel::class.java)
        // TODO: Use the ViewModel

        lifecycleScope.launch{
            viewModel.getNewsData(requireContext())
        }

        viewModel.news.observe(viewLifecycleOwner,Observer{ it ->
            recyclerView = binding.recyclerViewNewsListAdmin
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager  = LinearLayoutManager(this.requireContext())

            adapter = NewsListAdapterForAdmin(it)
            recyclerView.adapter = adapter

            adapter.onItemClick = {
                val preferences = this.requireActivity().getSharedPreferences("pass", Context.MODE_PRIVATE)
                val editor =preferences.edit()

                editor.putString("id",it.newsID.toString())
                editor.putString("tittle" , it.title.toString())
                editor.apply()
                editor.commit()

                findNavController().navigate(NewsListAdminFragmentDirections.actionNewsListAdminFragmentToUpdateNewsAdmin())
            }
        })

    }

    fun searchReqFormID(){
        if(validateSearchInput()){
            viewModel.searchNews(requireContext(),binding.editSearchNews.text.toString().trim())

            viewModel.newsSearchList.observe(viewLifecycleOwner, Observer { it ->
                adapter.setDataToAdapter(it)
                //set toast if empty list
                if (adapter.getItemCount() == 0)
                {
                    Toast.makeText(getActivity(), "No News List Found!", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            viewModel.news.observe(viewLifecycleOwner,Observer{
                adapter.setDataToAdapter(it)
            })
            Toast.makeText(getActivity(), "Search Field Is Empty!", Toast.LENGTH_SHORT).show()
        }

    }

    fun validateSearchInput(): Boolean {
        return binding.editSearchNews.text.isNotEmpty()
    }




}