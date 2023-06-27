package com.example.storymapps.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storymapps.data.model.UserModel
import com.example.storymapps.data.online.response.ListStoryItem
import com.example.storymapps.data.repository.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: StoryRepository
): ViewModel() {

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> = repository.getStories(token).cachedIn(viewModelScope)
    fun getUser(): LiveData<UserModel> = repository.getUser()

    fun logout() {
        viewModelScope.launch {
            repository.deleteUser()
        }
    }
}