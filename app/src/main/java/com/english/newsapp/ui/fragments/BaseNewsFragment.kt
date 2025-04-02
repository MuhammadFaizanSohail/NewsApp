package com.english.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.english.newsapp.R
import com.english.newsapp.data.model.NewsState
import com.english.newsapp.databinding.FragmentNewsBinding
import com.english.newsapp.ui.adapters.NewsAdapter
import com.english.newsapp.ui.component.dialog.ErrorDialog
import com.english.newsapp.ui.component.dialog.ProgressDialog
import com.english.newsapp.ui.viewmodel.NewsViewModel
import com.english.newsapp.utils.extensions.checkNetwork
import com.english.newsapp.utils.extensions.createErrorDialog
import com.english.newsapp.utils.extensions.createProgressDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
abstract class BaseNewsFragment : Fragment() {

    protected val newsViewModel: NewsViewModel by activityViewModels()
    private var progressDialog: ProgressDialog? = null
    private var errorDialog : ErrorDialog?= null
    protected lateinit var binding: FragmentNewsBinding
    protected val newsAdapter by lazy { NewsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isAdded && activity != null) {
            progressDialog = requireContext().createProgressDialog()
            errorDialog = requireContext().createErrorDialog()
            setupRecyclerView()
            setupObservers()
            setupRefreshListener()
            fetchNews(false)
        }
    }

    private fun setupRecyclerView() = with(binding.recyclerView) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = newsAdapter
    }

    private fun setupRefreshListener() = with(binding.swipeRefreshLayout) {
        setOnRefreshListener {
            isRefreshing = false
            if (requireContext().checkNetwork()) {
                fetchNews(true)
            } else {
                showError(getString(R.string.no_internet))
            }
        }
    }

    protected fun handleState(state: NewsState) {
        when (state) {
            is NewsState.Loading -> progressDialog?.show()

            is NewsState.Error -> {
                progressDialog?.dismiss()
                showError(state.message)
                Log.e("NewsError", state.message)
            }

            is NewsState.Success -> {
                progressDialog?.dismiss()
                newsAdapter.articlesList = state.articles.toMutableList()
            }
        }
    }

    protected fun showError(message: String) {
        errorDialog?.apply {
            setErrorMessage(message)
            show()
        }
    }

    abstract fun setupObservers()
    abstract fun fetchNews(isRefreshing: Boolean)
}
