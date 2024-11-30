package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

data class MovieChooseState(
    val movies: List<Genre> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedMovies: List<Boolean> = emptyList()
)