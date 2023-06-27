package com.example.storymapps.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storymapps.data.Result
import com.example.storymapps.data.StoryRemoteMediator
import com.example.storymapps.data.database.StoryDatabase
import com.example.storymapps.data.model.UserModel
import com.example.storymapps.data.model.UserPreference
import com.example.storymapps.data.online.api.ApiService
import com.example.storymapps.data.online.response.ListStoryItem
import com.example.storymapps.data.online.response.LoginResponse
import com.example.storymapps.data.online.response.RegisterResponse
import com.example.storymapps.data.online.response.StoryResponse
import com.example.storymapps.data.online.response.UploadStoryResponse
import com.example.storymapps.ui.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepository (
    private val preference: UserPreference,
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
){

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (!(response.error)) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: Exception) {
            Log.d("StoRepo:login", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("StoRepo:register", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun addStory(token: String, addFile: File, desc: String, lat: Double?, lon: Double?): LiveData<Result<UploadStoryResponse>> = liveData {
        emit(Result.Loading)

        val file = Utils.reduceFileImage(addFile)
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        try {
            val response = apiService.addStory(token, imageMultipart, description, lat, lon)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("StoRepo: addStory", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoriesWithLocation(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories(token, location = 1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("StoRepo:getWithLoc", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUser() = preference.getUser().asLiveData()
    suspend fun saveUser(userModel: UserModel) = preference.saveUser(userModel)
    suspend fun deleteUser() = preference.deleteUser()

    fun getTheme() = preference.getThemeSetting().asLiveData()
    suspend fun saveTheme(isDarkMode: Boolean) = preference.saveThemeSetting(isDarkMode)

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preference: UserPreference,
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preference, storyDatabase, apiService)
            }.also { instance = it }
    }
}