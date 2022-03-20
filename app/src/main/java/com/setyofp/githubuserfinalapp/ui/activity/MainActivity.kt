package com.setyofp.githubuserfinalapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.setyofp.githubuserfinalapp.R
import com.setyofp.githubuserfinalapp.database.GithubUser
import com.setyofp.githubuserfinalapp.databinding.ActivityMainBinding
import com.setyofp.githubuserfinalapp.ui.ResponseData
import com.setyofp.githubuserfinalapp.ui.ViewState
import com.setyofp.githubuserfinalapp.ui.adapter.ListUserAdapter

class MainActivity : AppCompatActivity(), ViewState<List<GithubUser>> {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var query: String
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var userAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        userAdapter = ListUserAdapter()
        mainBinding.searchMain.listUser.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }

        mainBinding.searchView.apply {
            queryHint = resources.getString(R.string.search_placeholder)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    this@MainActivity.query = query.toString()
                    clearFocus()
                    viewModel.searchUser(this@MainActivity.query).observe(this@MainActivity) {
                        when (it) {
                            is ResponseData.Error -> onFailed(it.message)
                            is ResponseData.Loading -> onLoading()
                            is ResponseData.Success -> it.data?.let { p -> onSuccess(p) }
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_settings -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    override fun onSuccess(data: List<GithubUser>) {
        userAdapter.setAllData(data)
        mainBinding.searchMain.apply {
            searchIcon.visibility = invisible
            instruction.visibility = invisible
            loadingCircle.visibility = invisible
            listUser.visibility = visible
        }
    }

    override fun onLoading() {
        mainBinding.searchMain.apply {
            searchIcon.visibility = invisible
            instruction.visibility = invisible
            loadingCircle.visibility = visible
            listUser.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        mainBinding.searchMain.apply {
            if (message == null) {
                searchIcon.apply {
                    setImageResource(R.drawable.ic_baseline_not_interested_24)
                    visibility = visible
                }
                instruction.apply {
                    text = resources.getString(R.string.user_not_found)
                    visibility = visible
                }
            } else {
                searchIcon.apply {
                    setImageResource(R.drawable.ic_baseline_search_off_24)
                    visibility = visible
                }
                instruction.apply {
                    text = message
                    visibility = visible
                }
            }
            loadingCircle.visibility = invisible
            listUser.visibility = invisible
        }
    }
}

