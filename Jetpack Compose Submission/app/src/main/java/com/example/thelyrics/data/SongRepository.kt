package com.example.thelyrics.data

import com.example.thelyrics.model.SongList
import com.example.thelyrics.model.SongsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SongRepository {
    private val songs = mutableListOf<SongList>()

    init {
        if (songs.isEmpty()) {
            SongsDataSource.songs.forEach {
                songs.add(SongList(it, false))
            }
        }
    }

    fun getSongs(): Flow<List<SongList>> {
        return flowOf(songs)
    }

    fun searchSongs(query: String): Flow<List<SongList>> {
        return flowOf(songs.filter {
            it.song.title.contains(query, ignoreCase = true)
        })
    }

    fun getDetail(idSong: Int): SongList {
        return songs.first {
            it.song.id == idSong
        }
    }

    fun addToFav(idSong: Int, isFav: Boolean): Flow<Boolean> {
        val index = songs.indexOfFirst { it.song.id == idSong }
        val result = if (index >= 0) {
            val song = songs[index]
            songs[index] = song.copy(
                song = song.song,
                isFavorite = isFav
            )
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getFavoriteSongs(): Flow<List<SongList?>> {
        return getSongs()
            .map { song ->
                song.filter { data ->
                    data.isFavorite
                }
            }
    }

    companion object {
        @Volatile
        private var instance: SongRepository? = null

        fun getInstance(): SongRepository =
            instance ?: synchronized(this) {
                SongRepository().apply {
                    instance = this
                }
            }
    }
}