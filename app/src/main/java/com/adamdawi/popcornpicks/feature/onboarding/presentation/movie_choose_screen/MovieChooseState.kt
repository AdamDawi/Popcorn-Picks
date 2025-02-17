package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen

import com.adamdawi.popcornpicks.core.domain.model.Movie

data class MovieChooseState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedMovies: List<Movie> = emptyList(),
    val finishButtonEnabled: Boolean = false
)