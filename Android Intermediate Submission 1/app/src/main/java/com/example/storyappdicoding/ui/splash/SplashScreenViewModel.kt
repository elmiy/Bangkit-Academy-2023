package com.example.storyappdicoding.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyappdicoding.data.model.UserModel
import com.example.storyappdicoding.data.model.UserPreference

class SplashScreenViewModel(private val pref : UserPreference): ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getTheme() = pref.getThemeSetting().asLiveData()
}