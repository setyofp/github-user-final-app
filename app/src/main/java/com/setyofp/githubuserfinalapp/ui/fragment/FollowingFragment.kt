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
import com.setyofp.githubuserfinalapp.databinding.FragmentFollowingBinding
import com.setyofp.githubuserfinalapp.ui.ResponseData
import com.setyofp.githubuserfinalapp.ui.ViewState
import com.setyofp.githubuserfinalapp.ui.adapter.ListUserAdapter

class FollowingFragment : Fragment(), ViewState<List<GithubUser>> {

    private lateinit var followingBinding: FragmentFollowingBinding
    private lateinit var viewModel: FollowingViewModel
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
        followingBinding = FragmentFollowingBinding.inflate(inflater, container, false)
        return followingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FollowingViewModel::class.java]
        userAdapter = ListUserAdapter()

        followingBinding.followingList.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewModel.getUserFollowing(username.toString()).observe(viewLifecycleOwner) {
            when (it) {
                is ResponseData.Error -> onFailed(it.message)
                is ResponseData.Loading -> onLoading()
                is ResponseData.Success -> it.data?.let { it1 -> onSuccess(it1) }
            }
        }
    }

    override fun onSuccess(data: List<GithubUser>) {
        userAdapter.setAllData(data)
        followingBinding.apply {
            instruction.visibility = invisible
            followingLoadingCircle.visibility = invisible
            followingList.visibility = visible
        }
    }

    override fun onLoading() {
        followingBinding.apply {
            instruction.visibility = invisible
            followingLoadingCircle.visibility = visible
            followingList.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        followingBinding.apply {
            if (message == null) {
                instruction.text = resources.getString(R.string.following_not_found, username)
                instruction.visibility = visible
            } else {
                instruction.text = message
                instruction.visibility = visible
            }
            followingLoadingCircle.visibility = invisible
            followingList.visibility = invisible
        }
    }

    companion object {
        private const val KEY_BUNDLE = "USERNAME"

        fun getInstance(username: String): Fragment {
            return FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_BUNDLE, username)
                }
            }
        }
    }
}
