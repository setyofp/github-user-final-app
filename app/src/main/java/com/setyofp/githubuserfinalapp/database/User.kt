package com.setyofp.githubuserfinalapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "User")
@Parcelize
data class GithubUser(
    @PrimaryKey(autoGenerate = true)
    @field:SerializedName("followers")
    @ColumnInfo(name = "followers")
    val followers: Int? = 0,

    @field:SerializedName("avatar_url")
    @ColumnInfo(name = "avatar")
    val avatar: String? = null,

    @field:SerializedName("following")
    @ColumnInfo(name = "following")
    val following: Int? = 0,

    @field:SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String? = null,

    @field:SerializedName("company")
    @ColumnInfo(name = "company")
    val company: String? = null,

    @field:SerializedName("location")
    @ColumnInfo(name = "location")
    val location: String? = null,

    @field:SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int? = 0,

    @field:SerializedName("public_repos")
    @ColumnInfo(name = "repository")
    val repository: Int? = 0,

    @field:SerializedName("login")
    @ColumnInfo(name = "username")
    val username: String? = null,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean? = false
) : Parcelable

data class SearchResponse(
    @field:SerializedName("items")
    val items: List<GithubUser>
)
