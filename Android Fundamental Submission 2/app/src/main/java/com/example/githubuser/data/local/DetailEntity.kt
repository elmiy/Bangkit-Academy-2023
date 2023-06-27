package com.example.githubuser.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailEntity(
    val username: String,
    val name: String?,
    val avatar: String,
    val followers: Int,
    val following: Int,
    val followersUrl: String,
    val followingUrl: String,
    val isFavorite: Boolean
): Parcelable