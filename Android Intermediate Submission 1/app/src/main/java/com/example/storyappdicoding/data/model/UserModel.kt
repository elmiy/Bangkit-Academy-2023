package com.example.storyappdicoding.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val name: String,
    val email: String,
    val token: String
) : Parcelable