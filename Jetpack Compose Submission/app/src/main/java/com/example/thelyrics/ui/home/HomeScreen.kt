package com.example.thelyrics.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.thelyrics.R
import com.example.thelyrics.di.Injection
import com.example.thelyrics.model.SongList
import com.example.thelyrics.ui.Result
import com.example.thelyrics.ui.ViewModelFactory
import com.example.thelyrics.ui.components.SongItem
import com.example.thelyrics.ui.theme.Primary1
import com.example.thelyrics.ui.theme.Secondary2
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Int) -> Unit,
) {
    viewModel.result.collectAsState(initial = Result.Loading).value.let { result ->
        when (result) {
            is Result.Loading -> {
                viewModel.getAllSong()
            }
            is Result.Success -> {
                HomeContent(
                    listSongs = result.data.sortedBy { it.song.title },
                    navigateToDetail = navigateToDetail,
                    viewModel = viewModel
                )
            }
            is Result.Error -> {}
        }
    }
}

@Composable
fun HomeContent(
    listSongs: List<SongList>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Int) -> Unit,
    viewModel: HomeViewModel
) {
    val query by viewModel.query

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Primary1,
                contentColor = Color.White,
                title = {
                    Text(
                        text = stringResource(id = R.string.home_page),
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
            .fillMaxSize()
        ) {
            Box(modifier = modifier.padding(16.dp)) {
                val scope = rememberCoroutineScope()
                val listState = rememberLazyListState()
                val showButton: Boolean by remember {
                    derivedStateOf { listState.firstVisibleItemIndex > 0 }
                }

                LazyColumn (
                    state = listState,
                    contentPadding = PaddingValues(bottom = 40.dp)
                ) {
                    item {
                        SearchBar(
                            query = query,
                            onQueryChange = viewModel::search,
                            modifier = Modifier
                                .background(Primary1)
                                .fillMaxWidth()
                                .padding(top = 0.dp)
                        )
                    }
                    items(listSongs) { data ->
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
                AnimatedVisibility(
                    visible = showButton,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically(),
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    ScrollToTopButton(
                        onClick = {
                            scope.launch {
                                listState.scrollToItem(index = 0)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text(stringResource(R.string.search_song))
        },
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Composable
fun ScrollToTopButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .shadow(elevation = 10.dp, shape = CircleShape)
            .clip(shape = CircleShape)
            .size(60.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Secondary2,
            contentColor = MaterialTheme.colors.primary
        )
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = null,
        )
    }
}