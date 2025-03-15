package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovie
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.util.Constants.Network.BASE_IMAGE_URL
import com.adamdawi.popcornpicks.core.presentation.theme.Blue
import com.adamdawi.popcornpicks.core.presentation.theme.Grey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.theme.Red
import com.adamdawi.popcornpicks.core.presentation.ui.ErrorScreen
import com.adamdawi.popcornpicks.core.presentation.ui.LoadingScreen
import com.adamdawi.popcornpicks.core.presentation.ui.ObserveAsEvents
import com.adamdawi.popcornpicks.core.presentation.ui.PopcornPicksTopAppBar
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.components.CircleIconButton
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.components.ImageScratch
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.utils.formatMovieDetails
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecommendationsScreen(
    viewModel: RecommendationsViewModel = koinViewModel<RecommendationsViewModel>(),
    onNavigateToProfile: () -> Unit,
    onNavigateToMovieDetails: (movieId: String) -> Unit
) {
    BackHandler {  }
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvents(viewModel.events) { event ->
        when(event){
            is RecommendationsEvent.Error ->{
                Toast.makeText(
                    context,
                    event.error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    when{
        state.value.isLoading -> LoadingScreen()
        state.value.error != null -> ErrorScreen(message = state.value.error)
        else ->{
            RecommendationsContent(
                state = state.value,
                onAction = { action ->
                    when (action) {
                        is RecommendationsAction.OnMoreInfoClicked -> onNavigateToMovieDetails(action.movieId)
                        is RecommendationsAction.OnProfileClicked -> onNavigateToProfile()
                        else -> viewModel.onAction(action)
                    }
                }
            )
        }
    }
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
            PopcornPicksTopAppBar(
                actions = {
                    IconButton(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape),
                        onClick = { onAction(RecommendationsAction.OnProfileClicked) }
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Icon",
                            tint = Color.White
                        )
                    }
                }
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
                isScratched = state.isMovieScratched,
                onScratchComplete = { onAction(RecommendationsAction.OnImageScratched) }
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
                onAction = onAction,
                isMovieLiked = state.isMovieLiked,
                movieId = state.recommendedMovie.id
            )
        }
    }
}

@Composable
fun ButtonsRow(
    modifier: Modifier = Modifier,
    areButtonsEnabled: Boolean,
    onAction: (RecommendationsAction) -> Unit,
    isMovieLiked: Boolean,
    movieId: Int
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
                onAction(RecommendationsAction.OnMoreInfoClicked(movieId.toString()))
            },
            enabled = areButtonsEnabled,
            contentDescription = "More info"
        )
        CircleIconButton(
            icon = if(isMovieLiked) painterResource(R.drawable.heart_solid_ic) else painterResource(R.drawable.heart_outlined_ic),
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
    movie: Movie
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
                recommendedMovie = dummyMovie,
                isMovieScratched = false
            ),
            onAction = {}
        )
    }
}