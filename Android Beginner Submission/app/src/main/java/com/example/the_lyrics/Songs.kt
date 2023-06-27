package com.example.the_lyrics

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Songs (
    val title: String,
    val singer: String,
    val description: String,
    val cover: String,
    val date: String,
    val lyrics: String
    ): Parcelable