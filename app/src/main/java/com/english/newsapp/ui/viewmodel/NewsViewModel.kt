package com.english.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.english.newsapp.data.model.NewsState
import com.english.newsapp.data.remote.model.Article
import com.english.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    private val _newsState = MutableStateFlow<NewsState>(NewsState.Loading)
    val newsState: StateFlow<NewsState> = _newsState.asStateFlow()

    private val _headlinesNewsState = MutableStateFlow<NewsState>(NewsState.Loading)
    val headlinesNewsState: StateFlow<NewsState> = _headlinesNewsState.asStateFlow()

    private val _searchQueryEverything = MutableStateFlow("")
    val searchQueryEverything: StateFlow<String> = _searchQueryEverything.asStateFlow()

    private val _searchQueryHeadlines = MutableStateFlow("")
    val searchQueryHeadlines: StateFlow<String> = _searchQueryHeadlines.asStateFlow()

    private var everythingArticles: List<Article> = emptyList()
    private var headlinesArticles: List<Article> = emptyList()

    fun clearSearchQueries() {
        setSearchQueryEverything("")
        setSearchQueryHeadlines("")
    }

    fun updateSearchQuery(position: Int, query: String) {
        when (position) {
            0 -> setSearchQueryEverything(query)
            1 -> setSearchQueryHeadlines(query)
        }
    }

    private fun setSearchQueryEverything(query: String) {
        _searchQueryEverything.value = query
        filterEverythingArticles()
    }

    private fun setSearchQueryHeadlines(query: String) {
        _searchQueryHeadlines.value = query
        filterHeadlinesArticles()
    }

    fun fetchEverythingNews(query: String, isRefreshing: Boolean = false) {
        fetchNews(query, isRefreshing, _newsState, repository::getEverything) { everythingArticles = it }
    }

    fun fetchHeadlinesNews(country: String, isRefreshing: Boolean = false) {
        fetchNews(country, isRefreshing, _headlinesNewsState, repository::getHeadlines) { headlinesArticles = it }
    }

    private fun fetchNews(
        query: String,
        isRefreshing: Boolean,
        stateFlow: MutableStateFlow<NewsState>,
        fetch: suspend (String, Boolean) -> List<Article>,
        updateCache: (List<Article>) -> Unit
    ) {
        viewModelScope.launch {
            stateFlow.emit(NewsState.Loading)
            try {
                val articles = fetch(query, isRefreshing)
                updateCache(articles)
                filterArticles(stateFlow, articles, query)
            } catch (e: Exception) {
                stateFlow.emit(NewsState.Error(e.message ?: "Failed to fetch news"))
            }
        }
    }

    private fun filterEverythingArticles() {
        filterArticles(_newsState, everythingArticles, searchQueryEverything.value)
    }

    private fun filterHeadlinesArticles() {
        filterArticles(_headlinesNewsState, headlinesArticles, searchQueryHeadlines.value)
    }

    private fun filterArticles(stateFlow: MutableStateFlow<NewsState>, articles: List<Article>, query: String) {
        val filteredList = if (query.isBlank()) articles else articles.filter { it.title.contains(query, ignoreCase = true) }
        stateFlow.value = NewsState.Success(filteredList)
    }
}
