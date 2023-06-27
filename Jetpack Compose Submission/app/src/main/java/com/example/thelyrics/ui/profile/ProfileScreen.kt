package com.example.thelyrics.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.thelyrics.R
import com.example.thelyrics.ui.theme.Primary1
import com.example.thelyrics.ui.theme.Primary2
import com.example.thelyrics.ui.theme.TheLyricsTheme

@Composable
fun ProfileScreen(
    name: String,
    email: String,
    photo: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Primary1,
                contentColor = Color.White,
                title = {
                    Text(
                        text = stringResource(id = R.string.about_page),
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Primary2)
                    .padding(32.dp)
            ) {
                AsyncImage(
                    model = photo,
                    contentDescription = stringResource(id = R.string.about_photo),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .padding(bottom = 8.dp)
                        .size(200.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.h5.copy(
                        fontFamily = FontFamily(Font(R.font.nunitosans_bold)),
                        color = Color.White
                    )
                )
                Text(
                    text = email,
                    modifier = modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.h6.copy(
                        fontFamily = FontFamily(Font(R.font.nunitosans_semibold)),
                        color = Color.White
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    TheLyricsTheme {
        ProfileScreen(
            name = "Elmifta Yuana",
            email = "meyuanaa@gmail.com",
            photo = ""
        )
    }
}