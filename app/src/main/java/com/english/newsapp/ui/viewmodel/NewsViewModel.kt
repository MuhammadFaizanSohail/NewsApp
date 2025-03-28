package com.english.newsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.english.newsapp.data.model.NewsState
import com.english.newsapp.data.model.NewsType
import com.english.newsapp.data.remote.model.request.EverythingQueryBuilder
import com.english.newsapp.data.remote.model.request.HeadlinesQueryBuilder
import com.english.newsapp.data.repository.NewsRepository
import com.english.newsapp.utils.constants.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    private val _newsState = MutableSharedFlow<NewsState>()
    val newsState: SharedFlow<NewsState> = _newsState

    fun fetchNews(
        type: NewsType,
        query: String? = null,
        country: String? = null
    ) {
        viewModelScope.launch {
            _newsState.emit(NewsState.Loading)
            try {
                val articles = when (type) {
                    NewsType.EVERYTHING -> {
                        requireNotNull(query) { "Query required for everything fetch" }
                        repository.getEverything(
                            EverythingQueryBuilder()
                                .query(query)
                                .apiKey(API_KEY),
                            query
                        )
                    }
                    NewsType.HEADLINES -> {
                        requireNotNull(country) { "Country required for headlines fetch" }
                        repository.getHeadlines(
                            HeadlinesQueryBuilder()
                                .country(country)
                                .apiKey(API_KEY),
                            country
                        )
                    }
                }
                _newsState.emit(NewsState.Success(articles))
            } catch (e: Exception) {
                _newsState.emit(NewsState.Error(e.message ?: "An error occurred"))
            }
        }
    }
}