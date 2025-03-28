package com.english.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.english.newsapp.data.model.NewsState
import com.english.newsapp.data.model.NewsType
import com.english.newsapp.databinding.FragmentHeadlineBinding
import com.english.newsapp.ui.adapters.NewsAdapter
import com.english.newsapp.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeadlinesFragment : Fragment() {
    private val newsViewModel: NewsViewModel by viewModels()

    private val binding by lazy {
        FragmentHeadlineBinding.inflate(layoutInflater)
    }

    private val newsAdapter by lazy {
        NewsAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialViews()
    }

    private fun setInitialViews() {
        setRecyclerView()
        fetchNews()
        collectNews()
    }

    private fun collectNews() = with(binding) {
        lifecycleScope.launch {
            newsViewModel.newsState.collect { state ->
                when (state) {
                    is NewsState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }

                    is NewsState.Error -> {
                        progressBar.visibility = View.GONE
                        Log.d("error***", "collectNews: ${state.message}")
                    }

                    is NewsState.Success -> {
                        newsAdapter.articlesList = state.articles.toMutableList()
                    }
                }
            }
        }
    }

    private fun fetchNews() {
        lifecycleScope.launch {
            newsViewModel.fetchNews(
                type = NewsType.HEADLINES,
                country = "us"
            )
        }
    }

    private fun setRecyclerView() = with(binding) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = newsAdapter
        }
    }
}