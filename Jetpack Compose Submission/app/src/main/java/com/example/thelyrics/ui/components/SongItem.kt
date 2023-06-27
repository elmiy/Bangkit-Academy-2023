package com.example.thelyrics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.thelyrics.R
import com.example.thelyrics.ui.theme.Primary2
import com.example.thelyrics.ui.theme.TheLyricsTheme

@Composable
fun SongItem(
    title: String,
    singer: String,
    imgAlbum: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(
                color = Primary2,
                shape = RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = imgAlbum,
            contentDescription = stringResource(id = R.string.album_cover),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .padding(16.dp)
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6.copy(
                    fontFamily = FontFamily(Font(R.font.nunitosans_bold)),
                    color = Color.White,
                    textAlign = TextAlign.Justify
                )
            )
            Text(
                text = singer,
                style = MaterialTheme.typography.subtitle2.copy(
                    fontFamily = FontFamily(Font(R.font.nunitosans_semibold)),
                    color = Color.White,
                    textAlign = TextAlign.Justify
                )
            )
        }
    }
}

@Preview (showBackground = true)
@Composable
fun SongItemPreview() {
    TheLyricsTheme {
        SongItem(
            title = "Chill",
            singer = "Stray Kids",
            imgAlbum = "")
    }
}
