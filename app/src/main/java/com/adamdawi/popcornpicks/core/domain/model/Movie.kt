package com.adamdawi.popcornpicks.core.domain.model

import com.adamdawi.popcornpicks.feature.onboarding.domain.model.Genre

data class Movie(
    val id: Int,
    val title: String,
    val poster: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val genres: List<Genre>
)