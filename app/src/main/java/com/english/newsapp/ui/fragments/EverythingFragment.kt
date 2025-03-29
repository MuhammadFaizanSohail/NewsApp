package com.english.newsapp.ui.fragments


import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EverythingFragment : BaseNewsFragment() {

    override fun setupObservers() {
        lifecycleScope.launch {
            newsViewModel.newsState.collect { handleState(it) }
        }
    }

    override fun fetchNews(isRefreshing: Boolean) {
        newsViewModel.fetchEverythingNews("apple", isRefreshing)
    }
}
