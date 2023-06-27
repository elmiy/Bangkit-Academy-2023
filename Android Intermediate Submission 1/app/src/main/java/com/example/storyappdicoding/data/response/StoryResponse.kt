package com.example.storyappdicoding.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryResponse(

    @SerializedName("listStory")
    val listStory: List<ListStoryItem>,

    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class ListStoryItem(
    @SerializedName("photoUrl")
    val photoUrl: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("lon")
    val lon: Double,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("lat")
    val lat: Double? = null
) : Parcelable