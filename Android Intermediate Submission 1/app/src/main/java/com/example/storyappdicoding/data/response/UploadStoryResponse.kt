package com.example.storyappdicoding.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadStoryResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String
) : Parcelable