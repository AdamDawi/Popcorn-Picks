package com.adamdawi.popcornpicks.feature.movie_details.presentation

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.core.presentation.theme.LightGrey
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
    ) {
        BackgroundImage(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .fadingEdge(),
            url = Constants.Network.BASE_IMAGE_URL + state.movie.backdrop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 220.dp)
                .padding(horizontal = 24.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = Constants.Network.BASE_IMAGE_URL + state.movie.poster,
                    contentDescription = "Movie poster",
                    modifier = Modifier
                        .width(140.dp)
                        .height(220.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    placeholder = painterResource(R.drawable.example_poster),
                    contentScale = ContentScale.Crop
                )
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(top = 80.dp)
                ) {
                    Row {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Rating",
                            color = LightGrey
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = state.movie.voteAverage.toString() + "/10",
                            color = LightGrey,
                        )
                    }

                    Row {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Released",
                            color = LightGrey
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = state.movie.releaseDate.take(7),
                            color = LightGrey,
                        )
                    }

                    Row {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Genre",
                            color = LightGrey
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = state.movie.genres[0].name,
                            color = LightGrey,
                        )
                    }

                    Row {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Runtime",
                            color = LightGrey
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = (state.movie.runtime / 60).toString() + "h " + (state.movie.runtime % 60).toString() + "m",
                            color = LightGrey,
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = state.movie.overview,
                color = Color.White
            )
        }
        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(32.dp)
                .clip(CircleShape)
                .clickable {
                    action(MovieDetailsAction.OnBackClick)
                },
            imageVector = Icons.AutoMirrored.Default.ArrowForward,
            contentDescription = "Back",
            tint = Color.White
        )
    }
}

@Composable
private fun BackgroundImage(
    modifier: Modifier = Modifier,
    url: String
) {
    AsyncImage(
        model = url,
        contentDescription = "Movie background image",
        modifier = modifier,
        placeholder = painterResource(R.drawable.example_backdrop), //TODO change placeholder
        contentScale = ContentScale.Crop,
        alpha = 0.6f
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