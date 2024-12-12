package com.adamdawi.popcornpicks.feature.recommendations.domain

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

data class RecommendedMovie(
    val id: Int,
    val title: String,
    val poster: String,
    val releaseDate: String,
    val voteAverage: Double,
    val popularity: Double,
    val genres: List<Genre>
)