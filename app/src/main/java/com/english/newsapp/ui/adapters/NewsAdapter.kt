package com.english.newsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.english.newsapp.data.remote.model.Article
import com.english.newsapp.databinding.AdapterItemEverythingBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    var articlesList: MutableList<Article> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class NewsViewHolder(private val binding: AdapterItemEverythingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) = with(binding) {
            title.text = article.title
            description.text = article.description
            Glide.with(title.context).load(article.urlToImage).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterItemEverythingBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articlesList[position])
    }

}