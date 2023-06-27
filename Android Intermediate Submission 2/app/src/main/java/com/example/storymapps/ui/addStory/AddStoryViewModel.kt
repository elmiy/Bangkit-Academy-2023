package com.example.storymapps.ui.addStory

import androidx.lifecycle.ViewModel
import com.example.storymapps.data.repository.StoryRepository
import java.io.File

class AddStoryViewModel(
    private val repository: StoryRepository
): ViewModel() {

    fun addStory(token: String, file: File, desc: String, lat: Double?, lon: Double?) =
        repository.addStory(token, file, desc, lat, lon)

}