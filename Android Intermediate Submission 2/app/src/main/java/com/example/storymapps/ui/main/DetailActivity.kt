package com.example.storymapps.ui.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storymapps.R
import com.example.storymapps.data.online.response.ListStoryItem
import com.example.storymapps.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.stories_page)

        setupAction()
    }

    private fun setupAction() {
        @Suppress("DEPRECATION")
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(STORY_EXTRA, ListStoryItem::class.java)
        } else {
            intent.getParcelableExtra(STORY_EXTRA)
        }

        binding.apply {
            Glide.with(this@DetailActivity)
                .load(data?.photoUrl)
                .into(binding.imgStory)
            binding.tvUser.text = data?.name
            binding.tvDesc.text = data?.description
        }
    }

    companion object {
        const val STORY_EXTRA = "story"
    }
}