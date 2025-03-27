package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun LikedMoviesScreen(
    viewModel: LikedMoviesViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
}