package com.adamdawi.popcornpicks.feature.recommendations.presentation

import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie

data class RecommendationsState(
    val recommendedMovie: Movie,
    val isMovieScratched: Boolean = false
)