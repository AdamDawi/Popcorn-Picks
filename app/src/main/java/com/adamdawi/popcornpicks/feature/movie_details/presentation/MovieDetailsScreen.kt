package com.adamdawi.popcornpicks.feature.movie_details.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    MovieDetailsContent(
        action = { action ->
            when (action) {
                is MovieDetailsAction.OnBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        },
        state = state.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailsContent(
    action: (MovieDetailsAction) -> Unit,
    state: MovieDetailsState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .background(Color.Black)
            ){
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/original/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
                    contentDescription = "Movie poster",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .fadingEdge(),
                    placeholder = painterResource(R.drawable.backdrop),
                    contentScale = ContentScale.Crop
                )
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/original/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                    contentDescription = "Movie poster",
                    modifier = Modifier
                        .padding(top = 120.dp, start = 60.dp)
                        .width(150.dp)
                        .height(250.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    placeholder = painterResource(R.drawable.poster),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(32.dp)
                .clip(CircleShape)
                .clickable{
                    action(MovieDetailsAction.OnBackClick)
                },
            imageVector = Icons.AutoMirrored.Default.ArrowForward,
            contentDescription = "Back",
            tint = Color.White
        )
    }
}

private fun Modifier.fadingEdge(): Modifier{
    val bottomFade = Brush.verticalGradient(0.7f to Color.Red, 1f to Color.Transparent)
    return this.graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = bottomFade, blendMode = BlendMode.DstIn)
    }
}

@Preview
@Composable
private fun MovieDetailsScreenPreview() {
    PopcornPicksTheme {
        MovieDetailsContent(
            action = {},
            state = MovieDetailsState()
        )
    }
}