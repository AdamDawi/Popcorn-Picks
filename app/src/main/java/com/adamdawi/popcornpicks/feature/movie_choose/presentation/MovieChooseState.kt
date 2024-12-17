package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie

data class MovieChooseState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedMovies: List<Movie> = emptyList(),
    val finishButtonEnabled: Boolean = false
)