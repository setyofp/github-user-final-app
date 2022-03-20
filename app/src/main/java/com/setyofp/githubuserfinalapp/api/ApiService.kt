package com.setyofp.githubuserfinalapp.api

import com.setyofp.githubuserfinalapp.database.SearchResponse
import com.setyofp.githubuserfinalapp.database.GithubUser
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("users/{username}")
    fun getUserDetail (
        @Path("username")
        username: String
    ): Call<GithubUser>

    @GET("users/{username}/followers")
    fun getUserFollowers (
        @Path("username")
        username: String
    ): Call<List<GithubUser>>

    @GET("users/{username}/following")
    fun getUserFollowing (
        @Path("username")
        username: String
    ): Call<List<GithubUser>>

    @GET("search/users")
    fun searchUsers (
        @Query("q")
        query: String
    ): Call<SearchResponse>
}

