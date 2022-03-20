package com.setyofp.githubuserfinalapp.database

import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(githubUser: GithubUser)

    @Query("SELECT * FROM user ORDER BY username ASC")
    suspend fun getList(): List<GithubUser>

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getDetail(username: String): GithubUser?

    @Delete
    suspend fun delete(githubUser: GithubUser)
}

