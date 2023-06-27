package com.example.storyappdicoding.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyappdicoding.data.api.ApiConfig
import com.example.storyappdicoding.data.model.UserModel
import com.example.storyappdicoding.data.model.UserPreference
import com.example.storyappdicoding.data.response.LoginResponse
import com.example.storyappdicoding.ui.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
    private val pref: UserPreference
) : ViewModel() {

    private val _isAnimate = MutableLiveData<Event<Boolean>>()
    val isAnimate: LiveData<Event<Boolean>> = _isAnimate

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoginError = MutableLiveData<Boolean>()
    val isLoginError: LiveData<Boolean> = _isLoginError

    init {
        _isAnimate.value = Event(false)
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _isLoginError.value = responseBody.error
                        val loginResult = responseBody.loginResult
                        val name = loginResult.name
                        val token = loginResult.token
                        viewModelScope.launch {
                            pref.saveUser(UserModel(name, email, token))
                        }
                    }
                } else {
                    _isLoginError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}