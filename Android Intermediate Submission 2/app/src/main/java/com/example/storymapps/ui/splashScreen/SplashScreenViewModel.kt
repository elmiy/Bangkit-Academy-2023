package com.example.storymapps.ui.splashScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storymapps.data.model.UserModel
import com.example.storymapps.data.repository.StoryRepository

class SplashScreenViewModel(
    private val repository: StoryRepository
): ViewModel() {
    fun getTheme() = repository.getTheme()

    fun getUser(): LiveData<UserModel> = repository.getUser()
}