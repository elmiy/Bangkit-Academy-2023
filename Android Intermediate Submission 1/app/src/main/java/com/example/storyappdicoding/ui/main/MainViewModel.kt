package com.example.storyappdicoding.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyappdicoding.data.api.ApiConfig
import com.example.storyappdicoding.data.model.UserModel
import com.example.storyappdicoding.data.model.UserPreference
import com.example.storyappdicoding.data.response.ListStoryItem
import com.example.storyappdicoding.data.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val pref: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getStories(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStories(token)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listStory.value = responseBody.listStory
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}