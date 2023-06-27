package com.example.the_lyrics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.the_lyrics.databinding.ActivityAboutPageBinding

class AboutPage : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_page)

        val bindingAbout = ActivityAboutPageBinding.inflate(layoutInflater)
        setContentView(bindingAbout.root)

        bindingAbout.btnBack.setOnClickListener(this)
        bindingAbout.tvNama.text = resources.getString(R.string.nama)
        bindingAbout.tvEmail.text = resources.getString(R.string.email)
        val linkImage: String = resources.getString(R.string.foto_diri)
        Glide.with(this).load(linkImage).into(bindingAbout.profileImage)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> {
                val intentBack = Intent(this@AboutPage, MainActivity::class.java)
                startActivity(intentBack)
            }
        }
    }
}