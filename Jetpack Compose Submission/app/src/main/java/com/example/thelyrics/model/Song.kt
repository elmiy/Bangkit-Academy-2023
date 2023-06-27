package com.example.thelyrics.model

data class Song (
    val id: Int,
    val title: String,
    val artist: String,
    val imgAlbum: String,
    val released: String,
    val desc: String,
    val lyric: String
)