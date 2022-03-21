package com.setyofp.githubuserfinalapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.setyofp.githubuserfinalapp.R
import com.setyofp.githubuserfinalapp.database.GithubUser
import com.setyofp.githubuserfinalapp.databinding.ActivityFavoriteBinding
import com.setyofp.githubuserfinalapp.ui.ResponseData
import com.setyofp.githubuserfinalapp.ui.ViewState
import com.setyofp.githubuserfinalapp.ui.adapter.ListUserAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(), ViewState<List<GithubUser>> {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private lateinit var userAdapter: ListUserAdapter
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        userAdapter = ListUserAdapter()

        favoriteBinding.favorite.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavoriteList().observe(this@FavoriteActivity) {
                when (it) {
                    is ResponseData.Error -> onFailed(it.message)
                    is ResponseData.Loading -> onLoading()
                    is ResponseData.Success -> it.data?.let { it1 -> onSuccess(it1) }
                }
            }
        }
    }

    override fun onSuccess(data: List<GithubUser>) {
        favoriteBinding.apply {
            favoriteLoadingCircle.visibility = invisible
        }
        userAdapter.setAllData(data)
    }

    override fun onLoading() {
        favoriteBinding.apply {
            favoriteLoadingCircle.visibility = visible
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavoriteList().observe(this@FavoriteActivity) {
                when (it) {
                    is ResponseData.Error -> onFailed(it.message)
                    is ResponseData.Loading -> onLoading()
                    is ResponseData.Success -> it.data?.let { p -> onSuccess(p) }
                }
            }
        }
    }

    override fun onFailed(message: String?) {
        if (message == null) {
            favoriteBinding.apply {
                favorite.visibility = invisible
                favoriteLoadingCircle.visibility = invisible
                favoriteEmpty.text = getString(R.string.favorite_empty)
            }
        }
    }
}
