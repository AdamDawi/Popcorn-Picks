package com.adamdawi.popcornpicks.feature.recommendations.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
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
import com.adamdawi.popcornpicks.core.dummy.dummyRecommendedMovie
import com.adamdawi.popcornpicks.core.theme.Blue
import com.adamdawi.popcornpicks.core.theme.Grey
import com.adamdawi.popcornpicks.core.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.theme.Red
import com.adamdawi.popcornpicks.core.utils.Constants.Network.BASE_IMAGE_URL
import com.adamdawi.popcornpicks.feature.recommendations.domain.RecommendedMovie
import com.adamdawi.popcornpicks.feature.recommendations.presentation.components.CircleIconButton
import com.adamdawi.popcornpicks.feature.recommendations.presentation.components.ImageScratch
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecommendationsScreen(
    viewModel: RecommendationsViewModel = koinViewModel<RecommendationsViewModel>()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    RecommendationsContent(
        state = state.value,
        onAction = { action ->
            when (action) {
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsContent(
    state: RecommendationsState,
    onAction: (RecommendationsAction) -> Unit,
) {
    val animatedAlpha = animateFloatAsState(targetValue = if (state.isMovieScratched) 1f else 0f)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                actions = {
                    IconButton(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape),
                        onClick = {

                        }
                    ){
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Icon",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
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
                overlayImage = ImageBitmap.imageResource(R.drawable.popcorn_overlay3),
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
                areButtonsEnabled = state.isMovieScratched
            )
        }

    }
}

@Composable
fun ButtonsRow(
    modifier: Modifier = Modifier,
    areButtonsEnabled: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxHeight(0.6f)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIconButton(
            icon = painterResource(R.drawable.more_info_ic),
            color = Blue,
            onClick = {},
            enabled = areButtonsEnabled
        )
        CircleIconButton(
            icon = painterResource(R.drawable.heart_ic),
            color = Red,
            onClick = {},
            enabled = areButtonsEnabled
        )
        CircleIconButton(
            icon = painterResource(R.drawable.retry_ic),
            color = Blue,
            onClick = {},
            enabled = areButtonsEnabled
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
            text = movie.releaseDate.take(4) + " · " +
                    movie.genres.getOrNull(0)?.name + "/" +
                    movie.genres.getOrNull(1)?.name + " · " +
                    movie.voteAverage + "/10",
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
                isMovieScratched = true
            ),
            onAction = {}
        )
    }
}