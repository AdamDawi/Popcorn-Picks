package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen

import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.data.dummy.selectedMovies
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LAZY_MOVIES_GRID
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.theme.fontFamily
import com.adamdawi.popcornpicks.core.presentation.ui.ErrorScreen
import com.adamdawi.popcornpicks.core.presentation.ui.LoadingScreen
import com.adamdawi.popcornpicks.core.presentation.ui.ObserveAsEvents
import com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.components.FinishFAB
import com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.components.MovieItem
import org.koin.androidx.compose.koinViewModel
import kotlin.math.abs

@Composable
fun MovieChooseScreen(
    onFinishClick: () -> Unit,
    viewModel: MovieChooseViewModel = koinViewModel<MovieChooseViewModel>()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MovieChooseEvent.Success -> {
                Toast.makeText(context, "Onboarding completed", Toast.LENGTH_SHORT).show()
                onFinishClick()
            }

            is MovieChooseEvent.Error -> {
                Toast.makeText(context, event.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    when {
        state.value.isLoading -> LoadingScreen()
        state.value.error != null -> ErrorScreen(message = state.value.error)
        state.value.movies.isEmpty() -> ErrorScreen(message = state.value.error)
        else ->
            MovieChooseContent(
                onAction = viewModel::onAction,
                state = state.value
            )
    }
}

@Composable
fun MovieChooseContent(
    onAction: (MovieChooseAction) -> Unit,
    state: MovieChooseState
) {
    val lazyListState = rememberLazyGridState()

    Scaffold(
        floatingActionButton = {
            FinishFAB(
                isTextExpanded = lazyListState.isScrollingUp(),
                onFinishClick = {
                    onAction(MovieChooseAction.OnFinishClick)
                },
                enabled = state.finishButtonEnabled
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
                        onAction(MovieChooseAction.ToggleMovieSelection(movie))
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
    selectedMovies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    lazyListState: LazyGridState
) {
    LazyVerticalGrid(
        modifier = Modifier.testTag(LAZY_MOVIES_GRID),
        state = lazyListState,
        columns = Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(moviesList, key = { _, movie -> movie.id }) { _, movie ->
            MovieItem(
                movie = movie,
                isSelected = selectedMovies.contains(movie),
                onClick = { onMovieClick(movie) }
            )
        }
    }
}

@Composable
private fun LazyGridState.isScrollingUp(threshold: Int = 50): Boolean {
    val previousIndex = remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    val previousScrollOffset = remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    val isScrollingUp = remember { mutableStateOf(true) }
    return remember(this) {
        derivedStateOf {
            if (
                previousIndex.intValue != firstVisibleItemIndex ||
                (abs(firstVisibleItemScrollOffset - previousScrollOffset.intValue) >= threshold)
            ) {
                isScrollingUp.value = (firstVisibleItemIndex < previousIndex.intValue) ||
                        (firstVisibleItemIndex == previousIndex.intValue &&
                                firstVisibleItemScrollOffset < previousScrollOffset.intValue)
                previousIndex.intValue = firstVisibleItemIndex
                previousScrollOffset.intValue = firstVisibleItemScrollOffset

            }
            isScrollingUp.value
        }
    }.value
}

@Preview
@Composable
private fun MovieChooseScreenPreview() {
    PopcornPicksTheme {
        MovieChooseContent(
            state = MovieChooseState(
                movies = dummyMovieList,
                selectedMovies = selectedMovies
            ),
            onAction = {}
        )
    }
}