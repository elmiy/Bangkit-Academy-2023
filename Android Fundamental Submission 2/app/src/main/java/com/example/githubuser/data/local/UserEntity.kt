package com.example.githubuser.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class UserEntity (
    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "avatar")
    val avatar: String,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean
): Parcelable