package com.example.thelyrics.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.thelyrics.R
import com.example.thelyrics.di.Injection
import com.example.thelyrics.ui.Result
import com.example.thelyrics.ui.ViewModelFactory
import com.example.thelyrics.ui.theme.Primary1
import com.example.thelyrics.ui.theme.Primary2
import com.example.thelyrics.ui.theme.Secondary2
import com.example.thelyrics.ui.theme.TheLyricsTheme

@Composable
fun DetailScreen(
    idSong: Int,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateBack: () -> Unit
) {
    viewModel.result.collectAsState(initial = Result.Loading).value.let { result ->
        when (result) {
            is Result.Loading -> {
                viewModel.getDetail(idSong)
            }
            is Result.Success -> {
                val data = result.data
                DetailContent(
                    title = data.song.title ,
                    singer = data.song.artist ,
                    imgAlbum = data.song.imgAlbum,
                    released = data.song.released,
                    desc = data.song.desc,
                    lyric = data.song.lyric,
                    isFav = data.isFavorite,
                    onAddToFav = {
                        viewModel.addToFav(data.song, it)
                    },
                    onBackClick = navigateBack
                )
            }
            is Result.Error -> {}
        }
    }
}

@Composable
fun DetailContent(
    title: String,
    singer: String,
    imgAlbum: String,
    released: String,
    desc: String,
    lyric: String,
    isFav: Boolean,
    onAddToFav: (isFav: Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isActive by rememberSaveable { mutableStateOf(isFav) }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Primary1,
                contentColor = Color.White,
                title = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.arrow_back),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { onBackClick() }
                    )
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.h6.copy(
                            fontFamily = FontFamily(Font(R.font.nunitosans_bold)),
                            color = Color.White
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isActive = !isActive
                    onAddToFav(isActive)
                },
                backgroundColor = Secondary2,
                contentColor = Color.White
            ){
                Icon(
                    imageVector = if (isActive) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(id = R.string.add_favorite)
                )
            }
        }
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = modifier
                    .background(
                        color = Primary2
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = modifier
                ) {
                    AsyncImage(
                        model = imgAlbum,
                        contentDescription = stringResource(id = R.string.album_cover),
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .size(160.dp)
                            .padding(start = 16.dp, top = 16.dp, bottom = 8.dp),
                        alignment = Alignment.TopStart
                    )
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.h4.copy(
                                fontFamily = FontFamily(Font(R.font.nunitosans_bold)),
                                color = Color.White
                            )
                        )
                        Text(
                            text = singer,
                            style = MaterialTheme.typography.subtitle1.copy(
                                fontFamily = FontFamily(Font(R.font.nunitosans_semibold)),
                                color = Color.White
                            )
                        )
                    }
                }
                Text(
                    modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    text = stringResource(id = R.string.released_year, released),
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontFamily = FontFamily(Font(R.font.nunitosans_regular)),
                        color = Color.White
                    )
                )
            }
            Text(
                text = desc,
                modifier = modifier
                    .padding(16.dp),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontFamily = FontFamily(Font(R.font.nunitosans_regular)),
                    color = Color.Gray,
                    textAlign = TextAlign.Justify
                )
            )
            Text(
                text = stringResource(id = R.string.lyric),
                modifier = modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.h5.copy(
                    fontFamily = FontFamily(Font(R.font.nunitosans_bold)),
                    color = Primary2
                )
            )
            Text(
                text = lyric,
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
                style = MaterialTheme.typography.subtitle2.copy(
                    fontFamily = FontFamily(Font(R.font.nunitosans_regular)),
                )
            )
        }}
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TheLyricsTheme {
        DetailContent(
            title = "double take",
            singer = "dhruv",
            imgAlbum = "",
            released = "2020",
            desc = "“double take” follows dhruv as he falls in love with one of his friends, and he wonders if his friend feels the same way. The single was the first released song by dhruv after a couple of SoundCloud releases.",
            lyric = "I could say I never dare\n" +
                    "To think about you in that way, but\n" +
                    "I would be lyin'\n" +
                    "And I pretend I'm happy for you\n" +
                    "When you find some dude to take home\n" +
                    "But I won't deny that\n" +
                    "In the midst of the crowds\n" +
                    "In the shapes in the clouds\n" +
                    "I don't see nobody but you\n" +
                    "In my rose-tinted dreams\n" +
                    "Wrinkled silk on my sheets\n" +
                    "I don't see nobody but you\n" +
                    "Boy, you got me hooked onto something\n" +
                    "Who could say that they saw us coming?\n" +
                    "Tell me\n" +
                    "Do you feel the love?\n" +
                    "Spend a summer or a lifetime with me\n" +
                    "Let me take you to the place of your dreams\n" +
                    "Tell me\n" +
                    "Do you feel the love?\n" +
                    "And I could say I never unzipped\n" +
                    "Those blue Levi's inside my head\n" +
                    "But that's far from the truth\n" +
                    "Don't know what's come over me\n" +
                    "It seems like yesterday when I said\n" +
                    "\"We'll be friends forever\"\n" +
                    "Constellations of stars\n" +
                    "Murals on city walls\n" +
                    "I don't see nobody but you\n" +
                    "You're my vice, you're my muse\n" +
                    "You're a nineteenth floor view\n" +
                    "I don't see nobody but you\n" +
                    "Boy, you got me hooked onto something\n" +
                    "Who could say that they saw us coming?\n" +
                    "Tell me\n" +
                    "Do you feel the love?\n" +
                    "Spend a summer or a lifetime with me\n" +
                    "Let me take you to the place of your dreams\n" +
                    "Tell me\n" +
                    "Do you feel the love?\n" +
                    "Boy, you got me hooked onto something\n" +
                    "Who could say that they saw us coming?\n" +
                    "Tell me\n" +
                    "Do you feel the love?\n" +
                    "Spend a summer or a lifetime with me\n" +
                    "Let me take you to the place of your dreams\n" +
                    "Tell me\n" +
                    "Do you feel the love?\n" +
                    "Do you feel the love?\n" +
                    "Do you feel the love?\n" +
                    "Do you feel the love?\n" +
                    "Do you feel the love?\n" +
                    "Feel the love\n" +
                    "Do you feel the love?",
            isFav = true,
            onAddToFav = {},
            onBackClick = {}
        )
    }
}