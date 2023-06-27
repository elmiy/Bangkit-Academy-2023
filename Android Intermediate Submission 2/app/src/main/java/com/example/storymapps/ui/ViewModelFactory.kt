package com.example.storymapps.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storymapps.data.di.Injection
import com.example.storymapps.data.repository.StoryRepository
import com.example.storymapps.ui.addStory.AddStoryViewModel
import com.example.storymapps.ui.login.LoginViewModel
import com.example.storymapps.ui.main.MainViewModel
import com.example.storymapps.ui.maps.MapsViewModel
import com.example.storymapps.ui.register.RegisterViewModel
import com.example.storymapps.ui.setting.SettingViewModel
import com.example.storymapps.ui.splashScreen.SplashScreenViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val repository: StoryRepository
): ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            RegisterViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            AddStoryViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            SettingViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
            SplashScreenViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            MapsViewModel(repository) as T
        } else throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}