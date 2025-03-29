package com.english.newsapp.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.english.newsapp.ui.fragments.EverythingFragment
import com.english.newsapp.ui.fragments.HeadlinesFragment

class NewsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment =
        if (position == 0) EverythingFragment() else HeadlinesFragment()

    override fun getItemCount(): Int = 2
}
