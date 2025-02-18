package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

import com.adamdawi.popcornpicks.core.domain.model.Movie

data class RecommendationsState(
    val recommendedMovie: Movie = Movie(0, "", "", "", 0.0, emptyList()),
    val recommendedMovies: List<Movie> = emptyList(),
    val isMovieScratched: Boolean = false,
    val isMovieLiked: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false
)