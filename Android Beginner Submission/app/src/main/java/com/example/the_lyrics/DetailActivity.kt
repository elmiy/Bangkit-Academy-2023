package com.example.the_lyrics

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.the_lyrics.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val KEY_SONGS = "key_songs"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val bindingDetail = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(bindingDetail.root)

        bindingDetail.btnBack.setOnClickListener(this)

        val dataSongs = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("key_songs", Songs::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(KEY_SONGS)
        }
        if (dataSongs != null) {
            bindingDetail.tvTitle.text = dataSongs.title
            bindingDetail.tvSinger.text = dataSongs.singer
            bindingDetail.tvDescription.text = dataSongs.description
            Glide.with(this).load(dataSongs.cover).into(bindingDetail.imgCover)
            bindingDetail.tvDate.text = "Released Year : ${dataSongs.date}"
            bindingDetail.tvLyric.text = dataSongs.lyrics
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                val intentBack = Intent(this@DetailActivity, MainActivity::class.java)
                startActivity(intentBack)
            }
        }
    }
}