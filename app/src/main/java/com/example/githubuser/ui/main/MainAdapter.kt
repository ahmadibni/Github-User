package com.example.githubuser.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.remote.response.ItemsItem
import com.example.githubuser.databinding.UserRowBinding
import com.example.githubuser.ui.detail.DetailActivity

class MainAdapter(private val listUser: List<ItemsItem>): RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.binding.tvUsernameMainList.text = user.login
        Glide.with(holder.binding.root)
            .load(user.avatarUrl)
            .into(holder.binding.ivProfileList)

        holder.itemView.setOnClickListener{
            val moveToDetailProfile = Intent(holder.itemView.context, DetailActivity::class.java)
            moveToDetailProfile.putExtra(DetailActivity.USERNAME, user.login)
            moveToDetailProfile.putExtra(DetailActivity.AVATAR, user.avatarUrl)
            holder.itemView.context.startActivity(moveToDetailProfile)
        }
    }

    override fun getItemCount(): Int = listUser.size

    class ViewHolder(var binding: UserRowBinding) : RecyclerView.ViewHolder(binding.root)
}