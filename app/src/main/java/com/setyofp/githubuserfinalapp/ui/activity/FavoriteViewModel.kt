package com.setyofp.githubuserfinalapp.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.setyofp.githubuserfinalapp.repository.UserRepository

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private val repository = UserRepository(application)

    suspend fun getFavoriteList() = repository.getList()
}
