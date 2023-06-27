package com.example.storymapps.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storymapps.data.repository.StoryRepository
import kotlinx.coroutines.launch

class SettingViewModel(
    private val repository: StoryRepository
): ViewModel() {

    fun getTheme() = repository.getTheme()

    fun saveTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            repository.saveTheme(isDarkMode)
        }
    }
}