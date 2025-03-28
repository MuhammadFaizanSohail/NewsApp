package com.english.newsapp.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.english.newsapp.R
import com.english.newsapp.databinding.ActivityMainBinding
import com.english.newsapp.ui.fragments.EverythingFragment
import com.english.newsapp.ui.fragments.HeadlinesFragment
import com.english.newsapp.ui.viewmodel.NewsViewModel
import com.english.newsapp.utils.extensions.setInsets
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val newsViewModel by viewModels<NewsViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setInsets(binding.root)
        setContentView(binding.root)
        setInitialViews()
    }

    private fun setInitialViews() {
        setViewpager()
    }

    private fun setViewpager() = with(binding) {
        val adapter = object : FragmentStateAdapter(this@MainActivity) {
            override fun createFragment(position: Int): Fragment {
                return if (position == 0) {
                    EverythingFragment()
                } else {
                    HeadlinesFragment()
                }
            }

            override fun getItemCount(): Int {
                return 2
            }
        }
        viewpager.setAdapter(adapter)
        val featuresArray = arrayOf(
            getString(R.string.everything),
            getString(R.string.headlines)
        )
        TabLayoutMediator(
            tabLayout, viewpager
        ) { tab: TabLayout.Tab, position: Int -> tab.setText(featuresArray[position]) }.attach()
    }
}