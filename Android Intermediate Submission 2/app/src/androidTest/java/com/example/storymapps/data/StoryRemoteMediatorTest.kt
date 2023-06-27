package com.example.storymapps.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storymapps.data.database.StoryDatabase
import com.example.storymapps.data.online.api.ApiService
import com.example.storymapps.data.online.response.ListStoryItem
import com.example.storymapps.data.online.response.LoginResponse
import com.example.storymapps.data.online.response.LoginResult
import com.example.storymapps.data.online.response.RegisterResponse
import com.example.storymapps.data.online.response.StoryResponse
import com.example.storymapps.data.online.response.UploadStoryResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*

import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(), StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
            "1"
        )
        val pagingState = PagingState<Int, ListStoryItem>(
            listOf(), null, PagingConfig(10), 10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return RegisterResponse(false, "success")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return LoginResponse(false, "success", LoginResult("1", "me", "1"))
    }

    override suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?
    ): UploadStoryResponse {
        return UploadStoryResponse(false, "success")
    }

    override suspend fun getStories(
        token: String, page: Int?, size: Int?, location: Int?
    ): StoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = i.toString(),
                name = "name $i",
                description = "description $i",
                photoUrl = "photo $i",
                createdAt = "created in $i",
                lat = i.toDouble(),
                lon = i.toDouble()
            )
            items.add(story)
        }

        return StoryResponse(items, false, "success")
    }
}