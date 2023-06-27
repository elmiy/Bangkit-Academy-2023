package com.example.storymapps.data.di

import android.content.Context
import com.example.storymapps.data.database.StoryDatabase
import com.example.storymapps.data.model.UserPreference
import com.example.storymapps.data.online.api.ApiConfig
import com.example.storymapps.data.repository.StoryRepository
import com.example.storymapps.ui.Utils.dataStore

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preference = UserPreference.getInstance(context.dataStore)
        val storyDatabase = StoryDatabase.getInstance(context)
        val apiService = ApiConfig.getApiService()

        return StoryRepository.getInstance(preference, storyDatabase, apiService)
    }
}