package com.english.newsapp.data.remote.model

import com.english.newsapp.data.model.Source


data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)
