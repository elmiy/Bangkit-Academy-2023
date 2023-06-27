package com.example.listof_users_github.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataUser(
    val login: String,
    val avatarUrl: String
): Parcelable