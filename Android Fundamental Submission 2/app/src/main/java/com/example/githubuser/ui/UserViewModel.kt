package com.example.githubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.UserRepository
import com.example.githubuser.data.local.DetailEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getUser() = userRepository.getUser()
    fun getSearchUser(uname: String) = userRepository.searchUser(uname)
    fun getDetail(uname: String) = userRepository.detailUser(uname)
    fun getFollowers(uname: String) = userRepository.followersUser(uname)
    fun getFollowing(uname: String) = userRepository.followingUser(uname)

    fun setFavorite(user: DetailEntity, isFav: Boolean) {
        viewModelScope.launch {
            userRepository.setFavoriteUser(user, isFav)
        }
    }
    fun getFavorite() = userRepository.getFavoriteUser()
    fun isUserFavorite(uname: String): Boolean = runBlocking {
        userRepository.isUserFavorite(uname)
    }
}