package com.example.foodhub.Logged.Admin.News

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodhub.R
import com.example.foodhub.database.News

class NewsListAdapterForAdmin (private var newsListAdminViewModel: List<News>)
    : RecyclerView.Adapter<NewsListAdapterForAdmin.NewsAdminListViewHolder>(){

    var onItemClick: ((News)-> Unit)? = null

    class NewsAdminListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.lbl_newsList_admin_title)
        val date : TextView = itemView.findViewById(R.id.lbl_createDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdminListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_news_item_list_admin, parent, false)
        return NewsAdminListViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdminListViewHolder, position: Int) {
       val news = newsListAdminViewModel[position]
        holder.title.setText(news.title.toString())
        var newCreatedDate = news.createdAt.toString().subSequence(0,10)
        holder.date.setText(newCreatedDate)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(news)
        }

    }

    override fun getItemCount(): Int {
        return newsListAdminViewModel.size
    }

    fun setDataToAdapter(reqFormList: List<News>) {
        newsListAdminViewModel = reqFormList as MutableList<News>
        notifyDataSetChanged()
    }

}