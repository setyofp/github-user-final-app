package com.setyofp.githubuserfinalapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayoutMediator
import com.setyofp.githubuserfinalapp.R
import com.setyofp.githubuserfinalapp.database.GithubUser
import com.setyofp.githubuserfinalapp.databinding.ActivityDetailBinding
import com.setyofp.githubuserfinalapp.ui.ResponseData
import com.setyofp.githubuserfinalapp.ui.ViewState
import com.setyofp.githubuserfinalapp.ui.adapter.FollowAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), ViewState<GithubUser?> {

    private lateinit var detailBinding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val username = intent.getStringExtra(EXTRA_USER)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getDetailUser(username.toString()).observe(this@DetailActivity) {
                when (it) {
                    is ResponseData.Error -> onFailed(it.message)
                    is ResponseData.Loading -> onLoading()
                    is ResponseData.Success -> onSuccess(it.data)
                }
            }
        }

        val pageAdapter = FollowAdapter(this, username.toString())

        detailBinding.apply {
            viewPager.adapter = pageAdapter
            TabLayoutMediator(tabs, viewPager) { tabs, position ->
                tabs.text = resources.getString(intArrayOf(
                    R.string.followers,
                    R.string.following
                )[position])
            }.attach()
        }
    }

    override fun onSuccess(data: GithubUser?) {
        detailBinding.apply {
            repositoryNumber.text = data?.repository.toString()
            followersNumber.text = data?.followers.toString()
            followingNumber.text = data?.following.toString()
            nameDetail.text = data?.name
            companyDetail.text = data?.company
            location.text = data?.location

            Glide.with(this@DetailActivity)
                .load(data?.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(avatarDetail)

            favorite.visibility = View.VISIBLE

            if (data?.isFavorite!!)
                favorite.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_24, null))
            else
                favorite.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_border_24, null))

            favorite.setOnClickListener {
                if (data.isFavorite!!) {
                    data.isFavorite = false
                    viewModel.deleteFavoriteUser(data)
                    favorite.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_border_24, null))
                } else {
                    data.isFavorite = true
                    data.let { it1 -> viewModel.insertFavoriteUser(it1) }
                    favorite.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_favorite_24, null))
                }
            }
            supportActionBar?.title = data.username
        }
    }

    override fun onLoading() {
        detailBinding.favorite.visibility = View.INVISIBLE
    }

    override fun onFailed(message: String?) {
        detailBinding.favorite.visibility = View.INVISIBLE
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}
