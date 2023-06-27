package com.example.the_lyrics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.the_lyrics.databinding.ItemRowSongBinding

class ListSongsAdapter (private val listSongs: ArrayList<Songs>): RecyclerView.Adapter<ListSongsAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Songs)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemRowSongBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowSongBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (title, singer, description, cover) = listSongs[position]
        holder.binding.tvTitle.text = title
        holder.binding.tvSinger.text = singer
        holder.binding.tvDescription.text = description
        Glide.with(holder.itemView.context).load(cover).into(holder.binding.imgCover)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listSongs[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listSongs.size
    }
}