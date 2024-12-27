package com.adamdawi.popcornpicks.feature.recommendations.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.data.dummy.dummyRecommendedMovie
import com.adamdawi.popcornpicks.core.domain.util.Constants.Network.BASE_IMAGE_URL
import com.adamdawi.popcornpicks.core.presentation.theme.Blue
import com.adamdawi.popcornpicks.core.presentation.theme.Grey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.theme.Red
import com.adamdawi.popcornpicks.feature.recommendations.domain.RecommendedMovie
import com.adamdawi.popcornpicks.feature.recommendations.presentation.components.CircleIconButton
import com.adamdawi.popcornpicks.feature.recommendations.presentation.components.ImageScratch
import com.adamdawi.popcornpicks.feature.recommendations.presentation.components.RecommendationsScreenTopAppBar
import com.adamdawi.popcornpicks.feature.recommendations.presentation.components.formatMovieDetails
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecommendationsScreen(
    viewModel: RecommendationsViewModel = koinViewModel<RecommendationsViewModel>(),
    onNavigateToProfile: () -> Unit,
    onNavigateToMovieDetails: () -> Unit
) {
    BackHandler {  }
    val state = viewModel.state.collectAsStateWithLifecycle()

    RecommendationsContent(
        state = state.value,
        onAction = { action ->
            when (action) {
                RecommendationsAction.OnMoreInfoClicked -> onNavigateToMovieDetails()
                RecommendationsAction.OnProfileClicked -> onNavigateToProfile()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun RecommendationsContent(
    state: RecommendationsState,
    onAction: (RecommendationsAction) -> Unit,
) {
    val animatedAlpha = animateFloatAsState(
        targetValue = if (state.isMovieScratched) 1f else 0f,
        label = "animated alpha"
    )
    Scaffold(
        topBar = {
            RecommendationsScreenTopAppBar(
                onProfileClicked = { onAction(RecommendationsAction.OnProfileClicked) }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 32.dp)
                .padding(scaffoldPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            ImageScratch(
                modifier = Modifier
                    .height(470.dp)
                    .fillMaxWidth(0.9f),
                overlayImage = ImageBitmap.imageResource(R.drawable.popcorn_overlay),
                baseImageUrl = BASE_IMAGE_URL + state.recommendedMovie.poster,
                isImageScratched = { state.isMovieScratched },
                onImageScratched = { onAction(RecommendationsAction.OnImageScratched) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            MovieDetails(
                modifier = Modifier.fillMaxWidth(0.9f),
                movie = state.recommendedMovie,
                alpha = animatedAlpha.value
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonsRow(
                areButtonsEnabled = state.isMovieScratched,
                onAction = onAction
            )
        }
    }
}

@Composable
fun ButtonsRow(
    modifier: Modifier = Modifier,
    areButtonsEnabled: Boolean,
    onAction: (RecommendationsAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxHeight(0.6f)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIconButton(
            icon = painterResource(R.drawable.letter_i_ic),
            color = Blue,
            onClick = {
                onAction(RecommendationsAction.OnMoreInfoClicked)
            },
            enabled = areButtonsEnabled,
            contentDescription = "More info"
        )
        CircleIconButton(
            icon = painterResource(R.drawable.heart_outlined_ic),
            color = Red,
            onClick = {
                onAction(RecommendationsAction.OnHeartClicked)
            },
            enabled = areButtonsEnabled,
            contentDescription = "Heart"
        )
        CircleIconButton(
            icon = painterResource(R.drawable.retry_ic),
            color = Blue,
            onClick = {
                onAction(RecommendationsAction.OnRerollClicked)
            },
            enabled = areButtonsEnabled,
            contentDescription = "Retry"
        )
    }
}

@Composable
fun MovieDetails(
    modifier: Modifier = Modifier,
    alpha: Float,
    movie: RecommendedMovie
) {
    Column(
        modifier = modifier
            .alpha(
                alpha
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = movie.title,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = movie.formatMovieDetails(),
            color = Grey,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun RecommendationsScreenPreview() {
    PopcornPicksTheme {
        RecommendationsContent(
            state = RecommendationsState(
                recommendedMovie = dummyRecommendedMovie,
                isMovieScratched = false
            ),
            onAction = {}
        )
    }
}