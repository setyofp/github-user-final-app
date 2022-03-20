package com.setyofp.githubuserfinalapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.setyofp.githubuserfinalapp.api.ApiConfig
import com.setyofp.githubuserfinalapp.api.ApiService
import com.setyofp.githubuserfinalapp.database.*
import com.setyofp.githubuserfinalapp.ui.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(application: Application) {

    private val retrofit : ApiService = ApiConfig.getApiService()
    private val dao : UserDao
    private val preferences : UserPreferences

    init {
        val database: UserRoomDatabase = UserRoomDatabase.getInstance(application)
        dao = database.userDao()
        preferences = UserPreferences.getInstance(application)
    }

    fun search(query: String): LiveData<ResponseData<List<GithubUser>>> {
        val listUser = MutableLiveData<ResponseData<List<GithubUser>>>()

        listUser.postValue(ResponseData.Loading())
        retrofit.searchUsers(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                val list = response.body()?.items
                if (list.isNullOrEmpty())
                    listUser.postValue(ResponseData.Error(null))
                else
                    listUser.postValue(ResponseData.Success(list))
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                listUser.postValue(ResponseData.Error(t.message))
            }

        })
        return listUser
    }

    suspend fun getDetail(username: String): LiveData<ResponseData<GithubUser>> {

        val user = MutableLiveData<ResponseData<GithubUser>>()

        if (dao.getDetail(username) != null) {
            user.postValue(ResponseData.Success(dao.getDetail(username)))
        } else {
            retrofit.getUserDetail(username).enqueue(object : Callback<GithubUser> {
                override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                    val result = response.body()
                    user.postValue(ResponseData.Success(result))
                }

                override fun onFailure(call: Call<GithubUser>, t: Throwable) = Unit
            })
        }
        return user
    }

    fun getFollowing(username: String): LiveData<ResponseData<List<GithubUser>>> {

        val listUser = MutableLiveData<ResponseData<List<GithubUser>>>()

        listUser.postValue(ResponseData.Loading())
        retrofit.getUserFollowing(username).enqueue(object : Callback<List<GithubUser>> {
            override fun onResponse(call: Call<List<GithubUser>>, response: Response<List<GithubUser>>) {
                val list = response.body()
                if (list.isNullOrEmpty())
                    listUser.postValue(ResponseData.Error(null))
                else
                    listUser.postValue(ResponseData.Success(list))
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                listUser.postValue(ResponseData.Error(t.message))
            }
        })
        return listUser
    }

    fun getFollowers(username: String): LiveData<ResponseData<List<GithubUser>>> {

        val listUser = MutableLiveData<ResponseData<List<GithubUser>>>()

        listUser.postValue(ResponseData.Loading())
        retrofit.getUserFollowers(username).enqueue(object : Callback<List<GithubUser>> {
            override fun onResponse(call: Call<List<GithubUser>>, response: Response<List<GithubUser>>) {
                val list = response.body()
                if (list.isNullOrEmpty())
                    listUser.postValue(ResponseData.Error(null))
                else
                    listUser.postValue(ResponseData.Success(list))
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                listUser.postValue(ResponseData.Error(t.message))
            }
        })
        return listUser
    }

    suspend fun getList(): LiveData<ResponseData<List<GithubUser>>> {

        val listFavorite = MutableLiveData<ResponseData<List<GithubUser>>>()
        listFavorite.postValue(ResponseData.Loading())

        if (dao.getList().isNullOrEmpty())
            listFavorite.postValue(ResponseData.Error(null))
        else
            listFavorite.postValue(ResponseData.Success(dao.getList()))

        return listFavorite
    }

    suspend fun insert(githubUser: GithubUser) = dao.insert(githubUser)

    suspend fun delete(githubUser: GithubUser) = dao.delete(githubUser)

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = preferences.saveThemeSetting(isDarkModeActive)

    fun getThemeSetting() = preferences.getThemeSetting()
}
