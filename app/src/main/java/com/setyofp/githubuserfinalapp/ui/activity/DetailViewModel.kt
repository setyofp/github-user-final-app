package com.setyofp.githubuserfinalapp.ui.activity

import android.app.Application
import androidx.lifecycle.*
import com.setyofp.githubuserfinalapp.database.GithubUser
import com.setyofp.githubuserfinalapp.repository.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): AndroidViewModel(application) {

    val repository = UserRepository(application)

    suspend fun getDetailUser(username: String) = repository.getDetail(username)

    fun insertFavoriteUser(githubUser: GithubUser) = viewModelScope.launch {
        repository.insert(githubUser)
    }

    fun deleteFavoriteUser(githubUser: GithubUser) = viewModelScope.launch {
        repository.delete(githubUser)
    }
}


