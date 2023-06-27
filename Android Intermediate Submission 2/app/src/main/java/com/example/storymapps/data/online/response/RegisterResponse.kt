package com.example.storymapps.data.online.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String
): Parcelable