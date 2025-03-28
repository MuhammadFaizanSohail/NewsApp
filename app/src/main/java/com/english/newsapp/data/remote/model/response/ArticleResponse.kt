package com.english.newsapp.data.remote.model.response

import com.english.newsapp.data.remote.model.Article

data class ArticleResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
