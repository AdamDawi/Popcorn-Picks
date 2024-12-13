package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovie
import com.adamdawi.popcornpicks.core.data.dummy.selectedMovies
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.theme.fontFamily
import com.adamdawi.popcornpicks.core.presentation.ui.ErrorScreen
import com.adamdawi.popcornpicks.core.presentation.ui.LoadingScreen
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import com.adamdawi.popcornpicks.feature.movie_choose.presentation.components.FinishFAB
import com.adamdawi.popcornpicks.feature.movie_choose.presentation.components.MovieItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieChooseScreen(
    onFinishClick: () -> Unit,
    viewModel: MovieChooseViewModel = koinViewModel<MovieChooseViewModel>()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    when {
        state.value.isLoading -> LoadingScreen()
        state.value.error != null -> ErrorScreen(message = state.value.error)
//        state.value.movies.isEmpty() -> ErrorScreen(message = state.value.error)
        else ->
            MovieChooseContent(
                onAction = { action ->
                    when (action) {
                        is MovieChooseAction.OnFinishClick -> onFinishClick()
                        else -> viewModel.onAction(action)
                    }
                },
                state = state.value
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieChooseContent(
    onAction: (MovieChooseAction) -> Unit,
    state: MovieChooseState
) {
    val lazyListState = rememberLazyGridState()
    var showContinueText = remember { mutableStateOf(true) }

    // Detect if the user is scrolling up or down
    val lastFirstVisibleItemIndex = remember { mutableIntStateOf(0) }
    val firstVisibleItemIndex = remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }

    LaunchedEffect(firstVisibleItemIndex.value) {
        val currentFirstVisibleItemIndex = lazyListState.firstVisibleItemIndex
        if (currentFirstVisibleItemIndex < lastFirstVisibleItemIndex.intValue) {
            showContinueText.value = true // Scrolling up
        } else if (currentFirstVisibleItemIndex > lastFirstVisibleItemIndex.intValue) {
            showContinueText.value = false // Scrolling down
        }
        lastFirstVisibleItemIndex.intValue = currentFirstVisibleItemIndex
    }

    Scaffold(
        floatingActionButton = {
            FinishFAB(
                showText = showContinueText.value,
                onFinishClick = {
                    if(state.selectedMovies.count { it } >= 2)
                    onAction(MovieChooseAction.OnFinishClick)
                },
                enabled = state.selectedMovies.count { it } >= 2
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                MovieChooseTitle()

                Spacer(modifier = Modifier.height(16.dp))

                MovieGrid(
                    moviesList = state.movies,
                    selectedMovies = state.selectedMovies,
                    onMovieClick = { movie ->
                        onAction(MovieChooseAction.SelectMovie(movie))
                    },
                    lazyListState = lazyListState
                )
            }
        }
    )
}

@Composable
private fun MovieChooseTitle() {
    Text(
        text = "Choose 3 or more of your favourite movies",
        fontFamily = fontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun MovieGrid(
    moviesList: List<Movie>,
    selectedMovies: List<Boolean>,
    onMovieClick: (Movie) -> Unit,
    lazyListState: LazyGridState
) {
    LazyVerticalGrid(
        state = lazyListState,
        columns = Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(moviesList) { index, movie ->
            val isSelected = if (index < selectedMovies.size) selectedMovies[index] else false
            MovieItem(
                movie = movie,
                isSelected = isSelected,
                onClick = { onMovieClick(movie) }
            )
        }
    }
}

@Preview
@Composable
private fun MovieChooseScreenPreview() {
    PopcornPicksTheme {
        MovieChooseContent(
            state = MovieChooseState(
                movies = buildList { repeat(10) { add(dummyMovie) } },
                selectedMovies = selectedMovies
            ),
            onAction = {}
        )
    }
}