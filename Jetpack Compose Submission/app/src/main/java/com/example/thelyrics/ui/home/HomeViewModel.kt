package com.example.thelyrics.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thelyrics.data.SongRepository
import com.example.thelyrics.model.SongList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.thelyrics.ui.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: SongRepository
) : ViewModel() {

    private val _result: MutableStateFlow<Result<List<SongList>>> = MutableStateFlow(Result.Loading)
    val result: StateFlow<Result<List<SongList>>> get() = _result

    fun getAllSong() {
        viewModelScope.launch {
            repository.getSongs()
                .catch {
                    _result.value = Result.Error(it.message.toString())
                }
                .collect { songs ->
                    _result.value = Result.Success(songs)
                }
        }
    }

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) {
        _query.value = newQuery
        viewModelScope.launch {
            repository.searchSongs(newQuery)
                .catch {
                    _result.value = Result.Error(it.message.toString())
                }
                .collect { songs ->
                    _result.value = Result.Success(songs)
                }
        }
    }

}