package com.example.githubuser.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.githubuser.data.local.DetailEntity
import com.example.githubuser.data.local.UserDao
import com.example.githubuser.data.local.UserEntity
import com.example.githubuser.data.online.ApiService
import com.example.githubuser.ui.MainViewModel

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val viewModel: MainViewModel
){
    fun getUser(): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUser(USER_ID)
            val user = response.items
            val userList = user.map {
                val isFavorite = userDao.isUserFavorite(it.login)
                UserEntity(
                    it.login,
                    it.avatarUrl,
                    isFavorite
                )
            }
            userDao.insertUser(userList)
        } catch (e: Exception) {
            Log.d("UserRepository", "getUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> = userDao.getUser().map { Result.Success(it) }
        emitSource(localData)
    }

    fun searchUser(query: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUser(query)
            val user = response.items
            val userList = user.map {
                val isFavorite = userDao.isUserFavorite(it.login)
                UserEntity(
                    it.login,
                    it.avatarUrl,
                    isFavorite
                )
            }
            viewModel.addUser(userList)
        } catch (e: Exception) {
            Log.d("UserRepository", "searchUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> = viewModel.getUser().map { Result.Success(it) }
        emitSource(localData)
    }

    fun detailUser(query: String): LiveData<Result<DetailEntity>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailUser(query)
            val isFavorite = userDao.isUserFavorite(response.login)
            val userList = DetailEntity(
                response.login,
                response.name,
                response.avatarUrl,
                response.followers,
                response.following,
                response.followersUrl,
                response.followingUrl,
                isFavorite
            )
            viewModel.addDetail(userList)
        } catch (e: Exception) {
            Log.d("UserRepository", "getDetailUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<DetailEntity>> = viewModel.getDetail().map { Result.Success(it) }
        emitSource(localData)
    }

    fun followersUser(query: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowers(query)
            val userList = response.map {
                val isFavorite = userDao.isUserFavorite(it.login)
                UserEntity(
                    it.login,
                    it.avatarUrl,
                    isFavorite
                )
            }
            viewModel.addFollowers(userList)
        } catch (e: Exception) {
            Log.d("UserRepository", "getFollowers: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> = viewModel.getFollowers().map { Result.Success(it) }
        emitSource(localData)
    }

    fun followingUser(query: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowing(query)
            val userList = response.map {
                val isFavorite = userDao.isUserFavorite(it.login)
                UserEntity(
                    it.login,
                    it.avatarUrl,
                    isFavorite
                )
            }
            viewModel.addFollowing(userList)
        } catch (e: Exception) {
            Log.d("UserRepository", "getFollowers: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> = viewModel.getFollowing().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> {
        return userDao.getFavoriteUsers()
    }

    suspend fun setFavoriteUser(user: DetailEntity, favoriteState: Boolean) {
        val userFave = UserEntity(
            user.username,
            user.avatar,
            favoriteState
        )
        if (!favoriteState) {
            userDao.updateUser(userFave)
        } else {
            userDao.updateUser(userFave)
            userDao.insertFavorite(userFave)
        }
    }

    suspend fun isUserFavorite(username: String): Boolean {
        return userDao.isUserFavorite(username)
    }


    companion object {
        private const val USER_ID = "elmi"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            viewModel: MainViewModel
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, viewModel)
            }.also { instance = it }
    }
}