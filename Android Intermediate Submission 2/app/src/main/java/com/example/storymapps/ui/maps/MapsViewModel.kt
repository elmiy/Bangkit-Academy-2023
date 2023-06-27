package com.example.storymapps.ui.maps

import androidx.lifecycle.ViewModel
import com.example.storymapps.data.repository.StoryRepository

class MapsViewModel(
    private val repository: StoryRepository
): ViewModel() {
    fun getStoryWithLocation(token: String) = repository.getStoriesWithLocation(token)
}