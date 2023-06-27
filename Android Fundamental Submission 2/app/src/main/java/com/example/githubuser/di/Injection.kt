package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.local.UserDatabase
import com.example.githubuser.data.online.ApiConfig
import com.example.githubuser.ui.MainViewModel

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val viewModel = MainViewModel()
        return UserRepository.getInstance(apiService, dao, viewModel)
    }
}