package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdawi.popcornpicks.core.data.dummy.dummyLikedMoviesList
import com.adamdawi.popcornpicks.core.presentation.theme.DividerGrey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.ui.PopcornPicksTopAppBar
import com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen.components.LikedMovieItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun LikedMoviesScreen(
    viewModel: LikedMoviesViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LikedMoviesContent(
        state = state.value,
        onAction = { action ->
            when (action) {
                LikedMoviesAction.OnBackClicked -> {
                    onNavigateBack()
                }
            }
            viewModel.onAction(action = action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LikedMoviesContent(
    state: LikedMoviesState,
    onAction: (LikedMoviesAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PopcornPicksTopAppBar(
                titleText = "Your Movies",
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                onAction(LikedMoviesAction.OnBackClicked)
                            },
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Arrow back",
                        tint = Color.White
                    )
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .padding(scaffoldPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(state.movies){ index, movie ->
                key(movie.id) {
                    LikedMovieItem(
                        movie = movie
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if(index != state.movies.size-1)
                        HorizontalDivider(thickness = 2.dp, color = DividerGrey.copy(alpha = .6f))
                }
            }
        }
    }
}

@Preview
@Composable
private fun LikedMoviesScreenPreview() {
    PopcornPicksTheme {
        LikedMoviesContent(
            state = LikedMoviesState(
                movies = dummyLikedMoviesList
            ),
            onAction = {}
        )
    }
}


