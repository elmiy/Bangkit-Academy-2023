package com.example.the_lyrics

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var rvSongs: RecyclerView
    private val listSong = ArrayList<Songs>()
    private lateinit var btnAboutPage: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvSongs = findViewById(R.id.rv_song)
        rvSongs.setHasFixedSize(true)

        listSong.addAll(getListSongs())
        showRecyclerList()

        btnAboutPage = findViewById(R.id.btn_about_page)
        btnAboutPage.setOnClickListener(this)
    }

    private fun getListSongs(): ArrayList<Songs> {
        val dataTitle = resources.getStringArray(R.array.data_titles)
        val dataSinger = resources.getStringArray(R.array.data_singers)
        val dataDescription = resources.getStringArray(R.array.data_descriptions)
        val dataCover = resources.getStringArray(R.array.data_cover)
        val dataDate = resources.getStringArray(R.array.data_dates)
        val dataLyrics = resources.getStringArray(R.array.data_lyrics)
        val listSongs = ArrayList<Songs>()
        for (i in dataTitle.indices) {
            val songs = Songs(dataTitle[i], dataSinger[i], dataDescription[i], dataCover[i], dataDate[i], dataLyrics[i])
            listSongs.add(songs)
        }
        return listSongs
    }

    private fun showRecyclerList() {
        rvSongs.layoutManager = LinearLayoutManager(this)
        val listSongsAdapter = ListSongsAdapter(listSong)
        rvSongs.adapter = listSongsAdapter
        listSongsAdapter.setOnItemClickCallback(object : ListSongsAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Songs) {
                showSelectedSongs(data)
            }
        })
    }

    private fun showSelectedSongs(songs: Songs) {
        val intentDetail = Intent(this@MainActivity, DetailActivity::class.java)
        intentDetail.putExtra(DetailActivity.KEY_SONGS, songs)
        startActivity(intentDetail)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_about_page -> {
                val intentAboutPage = Intent(this@MainActivity, AboutPage::class.java)
                startActivity(intentAboutPage)
            }
        }
    }
}