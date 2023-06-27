package com.example.storyappdicoding.ui.addStory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyappdicoding.data.api.ApiConfig
import com.example.storyappdicoding.data.model.UserModel
import com.example.storyappdicoding.data.model.UserPreference
import com.example.storyappdicoding.data.response.UploadStoryResponse
import com.example.storyappdicoding.ui.Utils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddStoryViewModel(
    private val pref: UserPreference
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _file = MutableLiveData<File>()
    val file: LiveData<File> = _file

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun setFile(file: File) {
        _file.value = file
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun addStory(token: String, desc: String) {
        _isLoading.value = true
        val img = Utils.reduceFileImage(_file.value as File)
        val description = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = img.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo", img.name, requestImageFile
        )

        val client = ApiConfig.getApiService().addStory(token, imageMultipart, description, null, null)
        client.enqueue(object : Callback<UploadStoryResponse> {
            override fun onResponse(
                call: Call<UploadStoryResponse>,
                response: Response<UploadStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.error) {
                            Log.e(TAG, "onFailure: ${response.message()}")
                        } else {
                            _success.value = true
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UploadStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}