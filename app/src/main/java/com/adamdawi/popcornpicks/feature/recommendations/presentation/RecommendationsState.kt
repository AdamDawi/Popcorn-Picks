package com.adamdawi.popcornpicks.feature.recommendations.presentation

import com.adamdawi.popcornpicks.feature.recommendations.domain.RecommendedMovie

data class RecommendationsState(
    val recommendedMovie: RecommendedMovie = RecommendedMovie(0, "", "", "", 0.0, emptyList()),
    val recommendedMovies: List<RecommendedMovie> = emptyList(),
    val isMovieScratched: Boolean = false,
    val isMovieLiked: Boolean = false
)