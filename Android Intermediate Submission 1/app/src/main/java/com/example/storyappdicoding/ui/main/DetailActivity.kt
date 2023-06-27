package com.example.storyappdicoding.ui.main

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyappdicoding.data.response.ListStoryItem
import com.example.storyappdicoding.ui.main.MainActivity.Companion.STORY_EXTRA
import com.example.storydicoding.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupAction()
    }

    private fun setupAction() {
        @Suppress("DEPRECATION") val item =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(STORY_EXTRA, ListStoryItem::class.java)
            } else {
                intent.getParcelableExtra(STORY_EXTRA)
            }
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(item?.photoUrl)
                .into(imgDetailPhoto)
            tvDetailName.text = item?.name
            tvDetailDescription.text = item?.description
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}