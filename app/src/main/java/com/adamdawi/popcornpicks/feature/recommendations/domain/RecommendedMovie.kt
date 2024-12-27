package com.adamdawi.popcornpicks.feature.recommendations.domain

import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre

data class RecommendedMovie(
    val id: Int,
    val title: String,
    val poster: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val genres: List<Genre>
)