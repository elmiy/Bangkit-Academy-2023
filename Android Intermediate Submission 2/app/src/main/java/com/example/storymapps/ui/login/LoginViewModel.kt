package com.example.storymapps.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storymapps.data.model.UserModel
import com.example.storymapps.data.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: StoryRepository
): ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)

    fun saveUser(userModel: UserModel) {
        viewModelScope.launch {
            repository.saveUser(userModel)
        }
    }
}