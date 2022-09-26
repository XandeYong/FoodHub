package com.example.foodhub.Logged.Admin.News

import java.util.ArrayList

data class NewsClass( val id:Int, val title:String)
{
    init {
        getNewsClass()
    }
    fun getNewsClass(): ArrayList<NewsClass> {
        var newsClassList = ArrayList<NewsClass>()
        newsClassList.add(NewsClass(1 , "this is 1"))
        newsClassList.add(NewsClass(1 , "this is 1"))
        newsClassList.add(NewsClass(1 , "this is 1"))
        newsClassList.add(NewsClass(1 , "this is 1"))
        newsClassList.add(NewsClass(1 , "this is 1"))
        return newsClassList
    }
}
