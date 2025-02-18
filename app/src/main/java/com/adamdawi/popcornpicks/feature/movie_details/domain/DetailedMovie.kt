package com.adamdawi.popcornpicks.feature.movie_details.domain

import com.adamdawi.popcornpicks.core.domain.model.Genre

data class DetailedMovie(
    val id: Int,
    val title: String,
    val poster: String,
    val genres: List<Genre>,
    val overview: String,
    val backdrop: String,
    val releaseDate: String,
    val voteAverage: Double,
    val runtime: Int,
    val popularity: Double
)