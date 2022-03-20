package com.setyofp.githubuserfinalapp.ui.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.setyofp.githubuserfinalapp.repository.UserRepository

class FollowingViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    fun getUserFollowing(username: String) = repository.getFollowing(username)
}

