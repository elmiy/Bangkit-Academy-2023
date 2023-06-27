package com.example.listof_users_github.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listof_users_github.data.DataUser
import com.example.listof_users_github.databinding.ItemUsersBinding

class UsersAdapter(private val listUsers: ArrayList<DataUser>): RecyclerView.Adapter<UsersAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: DataUser)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder (var binding: ItemUsersBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (login, avatarUrl) = listUsers[position]
        holder.binding.tvUsers.text = login
        Glide.with(holder.itemView.context).load(avatarUrl).into(holder.binding.imgProfpic)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUsers[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }
}