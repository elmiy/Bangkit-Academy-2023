package com.example.thelyrics.ui.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thelyrics.R
import com.example.thelyrics.di.Injection
import com.example.thelyrics.model.SongList
import com.example.thelyrics.ui.Result
import com.example.thelyrics.ui.ViewModelFactory
import com.example.thelyrics.ui.components.SongItem
import com.example.thelyrics.ui.theme.Primary1
import com.example.thelyrics.ui.theme.TheLyricsTheme

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Int) -> Unit,
) {
    viewModel.result.collectAsState(initial = Result.Loading).value.let { result ->
        when (result) {
            is Result.Loading -> {
                viewModel.getFavoriteSongs()
            }
            is Result.Success -> {
                result.data.favSongs.let { data ->
                    FavoriteContent(
                        listSongs = data.sortedBy { it?.song?.title },
                        navigateToDetail = navigateToDetail
                    )
                }
            }
            is Result.Error -> {}
        }
    }
}

@Composable
fun FavoriteContent(
    listSongs: List<SongList?>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Primary1,
                contentColor = Color.White,
                title = {
                    Text(
                        text = stringResource(id = R.string.favorite_page),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = modifier.padding(start = 16.dp),
                        style = MaterialTheme.typography.h6.copy(
                            fontFamily = FontFamily(Font(R.font.nunitosans_bold)),
                            color = Color.White
                        )
                    )
                }
            )
        }
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            Box(modifier = modifier.padding(16.dp)) {
                val listState = rememberLazyListState()

                LazyColumn (
                    state = listState
                ) {
                    items(listSongs) { data ->
                        if (data != null) {
                            SongItem(
                                title = data.song.title,
                                singer = data.song.artist,
                                imgAlbum = data.song.imgAlbum,
                                modifier = modifier
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        navigateToDetail(data.song.id)
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePreview() {
    TheLyricsTheme {
        FavoriteContent(
            listSongs = listOf(),
            navigateToDetail = {}
        )
    }
}