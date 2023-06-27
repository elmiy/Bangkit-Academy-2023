package com.example.thelyrics.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thelyrics.data.SongRepository
import com.example.thelyrics.model.Song
import com.example.thelyrics.model.SongList
import com.example.thelyrics.ui.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: SongRepository
) : ViewModel() {

    private val _result: MutableStateFlow<Result<SongList>> = MutableStateFlow(Result.Loading)
    val result: StateFlow<Result<SongList>> get() = _result

    fun getDetail(idSong: Int) =
        viewModelScope.launch {
            _result.value = Result.Loading
            _result.value = Result.Success(repository.getDetail(idSong))
        }

    fun addToFav(song: Song, isFav: Boolean) {
        viewModelScope.launch {
            repository.addToFav(song.id, isFav)
        }
    }

}