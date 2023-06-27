package com.example.listof_users_github.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listof_users_github.api.ApiConfig
import com.example.listof_users_github.data.DetailUserResponse
import com.example.listof_users_github.data.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {

    companion object{
        private const val TAG = "DetailViewModel"
    }

    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _userDetail

    private val _userFollowers = MutableLiveData<List<ItemsItem>>()
    val userFollowers: LiveData<List<ItemsItem>> = _userFollowers

    private val _userFollowing = MutableLiveData<List<ItemsItem>>()
    val userFollowing: LiveData<List<ItemsItem>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun detailUser(uname: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(uname)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    Log.e(TAG, "onFailureb: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailurea: ${t.message}")
            }
        })
    }

    fun followersUser(uname: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(uname)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                } else {
                    Log.e(TAG, "onFailuresc: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "onFailures: ${t.message}")
            }
        })
    }

    fun followingUser(uname: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(uname)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        _userFollowing.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}