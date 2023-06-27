package com.example.githubuser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.local.DetailEntity
import com.example.githubuser.data.local.UserEntity

class MainViewModel: ViewModel() {
    private val _usersearch = MutableLiveData<List<UserEntity>>()
    private val usersearch: LiveData<List<UserEntity>> = _usersearch

    private val _displayuser = MutableLiveData<DetailEntity>()
    private val displayuser: LiveData<DetailEntity>  = _displayuser

    private val _followers = MutableLiveData<List<UserEntity>>()
    private val followers: LiveData<List<UserEntity>> = _followers

    private val _following = MutableLiveData<List<UserEntity>>()
    private val following: LiveData<List<UserEntity>> = _following

    fun addUser(list : List<UserEntity>) {
        _usersearch.postValue(list)
    }

    fun getUser() : LiveData<List<UserEntity>> = usersearch

    fun addDetail(detail: DetailEntity) {
        _displayuser.postValue(detail)
    }

    fun getDetail(): LiveData<DetailEntity> = displayuser

    fun addFollowers(list : List<UserEntity>) {
        _followers.postValue(list)
    }

    fun getFollowers() : LiveData<List<UserEntity>> = followers

    fun addFollowing(list : List<UserEntity>) {
        _following.postValue(list)
    }

    fun getFollowing() : LiveData<List<UserEntity>> = following

}
