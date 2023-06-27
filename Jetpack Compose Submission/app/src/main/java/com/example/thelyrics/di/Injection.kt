package com.example.thelyrics.di

import com.example.thelyrics.data.SongRepository

object Injection {
    fun provideRepository(): SongRepository {
        return SongRepository.getInstance()
    }
}