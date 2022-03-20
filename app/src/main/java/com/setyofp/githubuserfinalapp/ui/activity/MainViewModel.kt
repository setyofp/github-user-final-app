package com.setyofp.githubuserfinalapp.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.setyofp.githubuserfinalapp.repository.UserRepository

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository = UserRepository(application)

    fun searchUser(query: String) = repository.search(query)
}
