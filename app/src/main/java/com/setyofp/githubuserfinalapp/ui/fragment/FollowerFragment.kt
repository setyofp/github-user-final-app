package com.setyofp.githubuserfinalapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.setyofp.githubuserfinalapp.R
import com.setyofp.githubuserfinalapp.database.GithubUser
import com.setyofp.githubuserfinalapp.databinding.FragmentFollowerBinding
import com.setyofp.githubuserfinalapp.ui.ResponseData
import com.setyofp.githubuserfinalapp.ui.ViewState
import com.setyofp.githubuserfinalapp.ui.adapter.ListUserAdapter

class FollowerFragment: Fragment(), ViewState<List<GithubUser>> {

    private lateinit var followerBinding: FragmentFollowerBinding
    private lateinit var viewModel: FollowerViewModel
    private lateinit var userAdapter: ListUserAdapter
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(KEY_BUNDLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followerBinding = FragmentFollowerBinding.inflate(inflater, container, false)
        return followerBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FollowerViewModel::class.java]
        userAdapter = ListUserAdapter()

        followerBinding.followerList.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.getUserFollowers(username.toString()).observe(viewLifecycleOwner) {
            when (it) {
                is ResponseData.Error -> onFailed(it.message)
                is ResponseData.Loading -> onLoading()
                is ResponseData.Success -> it.data?.let { it1 -> onSuccess(it1) }
            }
        }

    }

    override fun onSuccess(data: List<GithubUser>) {
        userAdapter.setAllData(data)
        followerBinding.apply {
            instruction.visibility = invisible
            followerLoadingCircle.visibility = invisible
            followerList.visibility = visible
        }
    }

    override fun onLoading() {
        followerBinding.apply {
            instruction.visibility = invisible
            followerLoadingCircle.visibility = visible
            followerList.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        followerBinding.apply {
            if (message == null) {
                instruction.text = resources.getString(R.string.followers_not_found, username)
                instruction.visibility = visible
            } else {
                instruction.text = message
                instruction.visibility = visible
            }
            followerLoadingCircle.visibility = invisible
            followerList.visibility = invisible
        }
    }

    companion object {
        private const val KEY_BUNDLE = "USERNAME"

        fun getInstance(username: String): Fragment {
            return FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUNDLE, username)
                }
            }
        }
    }
}
