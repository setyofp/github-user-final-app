package com.setyofp.githubuserfinalapp.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.setyofp.githubuserfinalapp.R
import com.setyofp.githubuserfinalapp.ui.fragment.FollowerFragment
import com.setyofp.githubuserfinalapp.ui.fragment.FollowingFragment

class FollowAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment.getInstance(username)
            1 -> fragment = FollowingFragment.getInstance(username)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return intArrayOf(
            R.string.followers,
            R.string.following
        ).size
    }
}

