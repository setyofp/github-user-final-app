package com.setyofp.githubuserfinalapp.ui.fragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.setyofp.githubuserfinalapp.repository.UserRepository

class FollowerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    fun getUserFollowers(username: String) = repository.getFollowers(username)
}


