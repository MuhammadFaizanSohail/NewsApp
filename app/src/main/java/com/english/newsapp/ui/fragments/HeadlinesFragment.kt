package com.english.newsapp.ui.fragments

import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadlinesFragment : BaseNewsFragment() {

    override fun setupObservers() {
        lifecycleScope.launch {
            newsViewModel.headlinesNewsState.collect { handleState(it) }
        }
    }

    override fun fetchNews(isRefreshing: Boolean) {
        newsViewModel.fetchHeadlinesNews("us", isRefreshing)
    }
}
