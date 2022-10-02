package com.example.foodhub.ui.news

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodhub.R
import com.example.foodhub.database.News
import java.util.*


class NewsAdapter (private val newsList: List<News>)
    : RecyclerView.Adapter<NewsAdapter.NewsAdminListViewHolder>(){

    var onItemClick: ((News)-> Unit)? = null

    class NewsAdminListViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.title_item_text_view)
        val date : TextView = itemView.findViewById(R.id.txt_date)
        val imgView : ImageView = itemView.findViewById(R.id.imageView_Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdminListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_new_list_item, parent, false)
        return NewsAdminListViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdminListViewHolder, position: Int) {
        val news = newsList[position]
        holder.title.setText(news.title)
        holder.imgView.load(news.image)
        var newCreatedDate = news.createdAt.toString().subSequence(0,10)
        holder.date.setText(newCreatedDate)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(news)
        }

    }

    override fun getItemCount(): Int {
        return newsList.size
    }


}