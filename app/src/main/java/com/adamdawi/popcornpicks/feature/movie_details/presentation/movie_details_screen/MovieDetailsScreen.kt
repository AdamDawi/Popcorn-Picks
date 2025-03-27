package com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.data.dummy.dummyDetailedMovie
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.core.presentation.theme.LightGrey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.ui.ErrorScreen
import com.adamdawi.popcornpicks.core.presentation.ui.LoadingScreen
import com.adamdawi.popcornpicks.core.presentation.ui.PosterImage
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.formatRuntime
import com.adamdawi.popcornpicks.core.presentation.ui.shimmerBrush
import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie
import com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen.MovieDetailsConstants.MAX_Y_OFFSET
import com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen.MovieDetailsConstants.POSTER_HEIGHT
import com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen.MovieDetailsConstants.POSTER_WIDTH
import com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen.components.TextValueRow
import com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen.components.rememberScrollConnection
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    when{
        state.value.isLoading -> LoadingScreen()
        state.value.error != null -> ErrorScreen(message = state.value.error)
        else ->
            MovieDetailsContent(
                action = { action ->
                    when (action) {
                        is MovieDetailsAction.OnBackClick -> onNavigateBack()
                    }
                },
                state = state.value
            )
    }
}

@Composable
private fun MovieDetailsContent(
    action: (MovieDetailsAction) -> Unit,
    state: MovieDetailsState
) {
    val currentOffsetY = remember { mutableFloatStateOf(0f) }
    val scrollState = rememberScrollState()
    val nestedScrollConnection = rememberScrollConnection(MAX_Y_OFFSET, currentOffsetY)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .nestedScroll(nestedScrollConnection)
    ) {
        BackgroundImage(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .fadingEdge(),
            url = Constants.Network.BASE_IMAGE_URL + state.movie.backdrop
        )
        OverviewSection(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = POSTER_HEIGHT*2 + currentOffsetY.floatValue.dp)
                .padding(horizontal = 24.dp)
                .then(
                    if(scrollState.canScrollForward || scrollState.canScrollBackward){
                        Modifier.verticalScroll(scrollState)
                    }else{
                        Modifier
                    }
                ),
            movieTitle = state.movie.title,
            movieOverview = state.movie.overview
        )
        PosterWithInfoSection(
            movie = state.movie,
            currentOffsetY = { currentOffsetY.floatValue }
        )
        BackIcon(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = {
                action(MovieDetailsAction.OnBackClick)
            }
        )
    }
}

@Composable
private fun BackgroundImage(
    modifier: Modifier = Modifier,
    url: String
) {
    val showShimmer = remember { mutableStateOf(true) }
    AsyncImage(
        modifier = modifier
            .background(shimmerBrush(showShimmer = showShimmer.value)),
        model = url,
        contentDescription = "Movie background image",
        onSuccess = { showShimmer.value = false },
        onError = {showShimmer.value = false},
        error = painterResource(R.drawable.no_poster),
        contentScale = ContentScale.Crop,
        alpha = 0.6f
    )
}

@Composable
private fun OverviewSection(
    modifier: Modifier = Modifier,
    movieTitle: String,
    movieOverview: String?
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = movieTitle,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = movieOverview ?: "No overview",
            color = LightGrey
        )
    }
}

@Composable
private fun PosterWithInfoSection(
    modifier: Modifier = Modifier,
    movie: DetailedMovie,
    currentOffsetY: ()-> Float
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(200.dp))
        Row(
            modifier = Modifier.offset(y = currentOffsetY().dp),
            horizontalArrangement = Arrangement.Center
        ) {
            PosterImage(
                modifier = Modifier
                    .width(POSTER_WIDTH)
                    .height(POSTER_HEIGHT),
                posterUrl = movie.poster.toString()
            )
            Spacer(
                modifier = Modifier
                    .width(20.dp)
            )
            MovieInfoColumn(
                modifier = Modifier.fillMaxWidth(),
                movie = movie
            )
        }
    }
}

@Composable
private fun MovieInfoColumn(
    modifier: Modifier = Modifier,
    movie: DetailedMovie
) {
    Column(
        modifier = modifier
            .padding(top = 80.dp)
    ) {
       TextValueRow(
           text = "Rating",
           value = if(movie.voteAverage != null) "%.2f".format(Locale.US, movie.voteAverage).toString() + "/10" else "???"
       )
        TextValueRow(
            text = "Released",
            value = movie.releaseDate?.take(7) ?: "???"
        )
        TextValueRow(
            text = "Genre",
            value = movie.genres.getOrNull(0)?.name ?: "???"

        )
        TextValueRow(
            text = "Runtime",
            value = movie.runtime?.let { formatRuntime(it) } ?: "???"
        )
    }
}

@Composable
private fun BackIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Icon(
        modifier = modifier
            .padding(16.dp)
            .size(36.dp)
            .clip(CircleShape)
            .clickable {
                onClick()

            },
        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
        contentDescription = "Back",
        tint = Color.White
    )
}

private fun Modifier.fadingEdge(): Modifier {
    val bottomFade = Brush.verticalGradient(0.7f to Color.Red, 1f to Color.Transparent)
    return this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(brush = bottomFade, blendMode = BlendMode.DstIn)
        }
}

private object MovieDetailsConstants {
    val MAX_Y_OFFSET = 100.dp.value
    val POSTER_HEIGHT = 220.dp
    val POSTER_WIDTH = 140.dp
}

@Preview
@Composable
private fun MovieDetailsScreenPreview() {
    PopcornPicksTheme {
        MovieDetailsContent(
            action = {},
            state = MovieDetailsState(
                movie = dummyDetailedMovie
            )
        )
    }
}