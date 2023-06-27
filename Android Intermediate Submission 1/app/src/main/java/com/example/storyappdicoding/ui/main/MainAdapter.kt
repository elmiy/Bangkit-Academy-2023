package com.example.storyappdicoding.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappdicoding.data.response.ListStoryItem
import com.example.storydicoding.databinding.ItemStoryBinding

class MainAdapter(private val listStoryItem: List<ListStoryItem>): RecyclerView.Adapter<MainAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStoryItem.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            tvUsers.text = listStoryItem[position].name
        }

        Glide.with(holder.itemView.rootView)
            .load(listStoryItem[position].photoUrl)
            .into(holder.binding.imgStory)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStoryItem[position])
        }
    }
}