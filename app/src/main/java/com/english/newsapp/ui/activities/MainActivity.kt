package com.english.newsapp.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.english.newsapp.R
import com.english.newsapp.databinding.ActivityMainBinding
import com.english.newsapp.ui.adapters.NewsPagerAdapter
import com.english.newsapp.ui.viewmodel.NewsViewModel
import com.english.newsapp.utils.extensions.hideKeyboard
import com.english.newsapp.utils.extensions.setInsets
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val newsViewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setInsets(binding.root)
        setContentView(binding.root)
        setupViewPager()
        setupSearchView()
    }

    private fun setupViewPager() = with(binding) {
        viewpager.adapter = NewsPagerAdapter(this@MainActivity)
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = getString(if (position == 0) R.string.everything else R.string.headlines)
        }.attach()

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                searchView.text.clear()
                newsViewModel.clearSearchQueries()
                hideKeyboard()
            }
        })
    }

    private fun setupSearchView() = with(binding) {
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let { query ->
                    newsViewModel.updateSearchQuery(viewpager.currentItem, query.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}

