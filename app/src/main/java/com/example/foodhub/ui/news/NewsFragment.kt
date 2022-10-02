package com.example.foodhub.ui.news


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.MainActivity
import com.example.foodhub.R
import com.example.foodhub.databinding.FragmentNewsBinding
import kotlinx.coroutines.launch


class NewsFragment : Fragment() {

    companion object {
        fun newInstance() = NewsFragment()
    }

    private lateinit var binding: FragmentNewsBinding
    private lateinit var viewModal: NewsViewModal

    private lateinit var recyclerView: RecyclerView

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewsBinding.inflate(inflater)
        Log.i("newsFragment", "ui/news start")

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i("newsFragment", "ui/news bye")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModal = ViewModelProvider(this).get(NewsViewModal::class.java)
        super.onActivityCreated(savedInstanceState)
        lifecycleScope.launch{
            viewModal.getNewsData(requireContext())
        }

        viewModal.news.observe(viewLifecycleOwner,Observer{ it ->
            recyclerView = binding.recyclerViewAllUser
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager  = LinearLayoutManager(this.requireContext())

            adapter = NewsAdapter(it)
            recyclerView.adapter = adapter

            adapter.onItemClick = {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data= Uri.parse(it.url)
                // start your next activity
                this.requireContext().startActivity(intent)
            }
        })

    }

}