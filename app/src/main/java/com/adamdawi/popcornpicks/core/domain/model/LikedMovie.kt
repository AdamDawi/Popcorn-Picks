package com.adamdawi.popcornpicks.core.domain.model

data class LikedMovie(
    val id: Int,
    val title: String,
    val poster: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val genres: List<Genre>,
    var nextPage: Int
)