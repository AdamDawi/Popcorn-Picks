package com.adamdawi.popcornpicks.feature.recommendations.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.dummy.dummyMovie
import com.adamdawi.popcornpicks.core.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.utils.Constants.Network.BASE_IMAGE_URL
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import com.adamdawi.popcornpicks.feature.recommendations.presentation.components.ImageScratch
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecommendationsScreen(
    viewModel: RecommendationsViewModel = koinViewModel<RecommendationsViewModel>()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    RecommendationsContent(
        recommendedMovie = dummyMovie,
        isMovieScratched = state.value.isMovieScratched,
        onAction = { action ->
            when (action) {
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun RecommendationsContent(
    onAction: (RecommendationsAction) -> Unit,
    recommendedMovie: Movie,
    isMovieScratched: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImageScratch(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(500.dp),
            overlayImage = ImageBitmap.imageResource(R.drawable.popcorn_overlay3),
            baseImageUrl = BASE_IMAGE_URL + recommendedMovie.poster,
            isImageScratched = { isMovieScratched },
            onImageScratched = { onAction(RecommendationsAction.OnImageScratched) }
        )
        //TODO fix animation
        AnimatedVisibility(
            visible = isMovieScratched
        ) {
            Column {
                Text(
                    text = recommendedMovie.title,
                    color = Color.White
                )
                Text(
                    text = recommendedMovie.releaseDate,
                    color = Color.White
                )
            }

        }
    }
}

@Preview
@Composable
private fun RecommendationsScreenPreview() {
    PopcornPicksTheme {
        RecommendationsContent(
            recommendedMovie = dummyMovie,
            isMovieScratched = false,
            onAction = {}
        )
    }
}