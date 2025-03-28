package com.english.newsapp.data.model

import com.english.newsapp.data.local.entities.ArticleEntity
import com.english.newsapp.data.remote.model.Article

sealed class NewsState {
    data object Loading : NewsState()
    data class Success(val articles: List<Article>) : NewsState()
    data class Error(val message: String) : NewsState()
}
