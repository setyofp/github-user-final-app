package com.setyofp.githubuserfinalapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.setyofp.githubuserfinalapp.database.GithubUser
import com.setyofp.githubuserfinalapp.databinding.ListUserBinding
import com.setyofp.githubuserfinalapp.ui.activity.DetailActivity
import com.setyofp.githubuserfinalapp.ui.activity.DetailActivity.Companion.EXTRA_USER

class ListUserAdapter: RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    private val listUser = ArrayList<GithubUser>()

    fun setAllData(data: List<GithubUser>) {
        listUser.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class UserViewHolder(private val view: ListUserBinding): RecyclerView.ViewHolder(view.root) {
        fun bind(githubUser: GithubUser?) {

            view.apply {
                username.text = githubUser?.username
            }

            Glide.with(itemView.context)
                .load(githubUser?.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(view.avatar)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_USER, githubUser?.username)
                itemView.context.startActivity(intent)
            }
        }
    }
}

