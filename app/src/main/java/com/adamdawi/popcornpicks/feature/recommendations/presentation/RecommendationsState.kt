package com.adamdawi.popcornpicks.feature.recommendations.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyRecommendedMovie
import com.adamdawi.popcornpicks.feature.recommendations.domain.RecommendedMovie

data class RecommendationsState(
    val recommendedMovie: RecommendedMovie = dummyRecommendedMovie,
    val isMovieScratched: Boolean = false
)