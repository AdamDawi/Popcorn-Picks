package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import com.adamdawi.popcornpicks.core.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.dummy.selectedMovies
import com.adamdawi.popcornpicks.core.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.theme.Red
import com.adamdawi.popcornpicks.core.theme.fontFamily
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import com.adamdawi.popcornpicks.feature.movie_choose.presentation.components.MovieItem

@Composable
fun MovieChooseScreen() {
    MovieChooseContent(
        moviesList = dummyMovieList,
        //TODO think about how to handle this selected state
        selectedMovies = selectedMovies
    )
}

@Composable
fun MovieChooseContent(
    moviesList: List<Movie>,
    selectedMovies: List<Boolean>
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
            ExtendedFloatingActionButton(
                expanded = showContinueText.value,
                onClick = {

                },
                containerColor = Red,
                text = {
                    Text(
                        text = "Finish",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                },
                icon = {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "Finish",
                        tint = Color.White
                    )
                }
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

                LazyVerticalGrid(
                    state = lazyListState,
                    columns = Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(moviesList){ index, movie ->
                        MovieItem(
                            movie = movie,
                            isSelected = selectedMovies[index],
                            onClick = {}
                        )
                    }
                }
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

@Preview
@Composable
private fun MovieChooseScreenPreview() {
    PopcornPicksTheme {
        MovieChooseContent(
            moviesList = dummyMovieList,
            selectedMovies = selectedMovies
        )
    }
}