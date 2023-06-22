package com.example.githubuser.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.detail.DetailListFragment

class SectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    var username: String = ""

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = DetailListFragment()
        fragment.arguments = Bundle().apply {
            putInt(DetailListFragment.ARG_POSITION, position+1)
            putString(DetailListFragment.ARG_USERNAME, username)
        }
        return fragment
    }
}