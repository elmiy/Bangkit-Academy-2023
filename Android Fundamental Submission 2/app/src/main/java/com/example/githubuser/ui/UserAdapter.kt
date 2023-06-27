package com.example.githubuser.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.local.UserEntity
import com.example.githubuser.databinding.ItemUsersBinding

class UserAdapter(private val onClick: (UserEntity) -> Unit) :
    ListAdapter<UserEntity, UserAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(var binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            binding.tvUsers.text = user.username
            Glide.with(itemView.context)
                .load(user.avatar)
                .into(binding.imgProfpic)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.binding.cardViewUser.setOnClickListener {
            onClick(user)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser.username == newUser.username
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}