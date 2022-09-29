package com.example.foodhub.Logged.Admin.News

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.News
import java.util.ArrayList

class NewsListAdminAdapterTest (private val newsListAdminViewModel: List<News>)
    : RecyclerView.Adapter<NewsListAdminAdapterTest.NewsAdminListViewHolder>(){

    var onItemClick: ((News)-> Unit)? = null

    class NewsAdminListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val fieldDonorIdDFLCL : TextView = itemView.findViewById(R.id.newsList_admin_title)
        val fieldDonationFormIdDFLCL : TextView = itemView.findViewById(R.id.newsList_admin_subtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdminListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_news_item_list_admin, parent, false)
        return NewsAdminListViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdminListViewHolder, position: Int) {
       val news = newsListAdminViewModel[position]
        holder.fieldDonorIdDFLCL.setText(news.newsID.toString())
        holder.fieldDonationFormIdDFLCL.setText(news.title)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(news)
        }

    }

    override fun getItemCount(): Int {
        return newsListAdminViewModel.size
    }


}