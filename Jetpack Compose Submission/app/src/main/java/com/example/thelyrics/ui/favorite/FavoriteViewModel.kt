package com.example.thelyrics.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thelyrics.data.SongRepository
import com.example.thelyrics.ui.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: SongRepository
) : ViewModel() {
    private val _result: MutableStateFlow<Result<FavoriteState>> = MutableStateFlow(Result.Loading)
    val result: StateFlow<Result<FavoriteState>> get() = _result

    fun getFavoriteSongs() {
        viewModelScope.launch {
            _result.value = Result.Loading
            repository.getFavoriteSongs()
                .collect { favSongs ->
                    _result.value = Result.Success(FavoriteState(favSongs))
                }
        }
    }
}