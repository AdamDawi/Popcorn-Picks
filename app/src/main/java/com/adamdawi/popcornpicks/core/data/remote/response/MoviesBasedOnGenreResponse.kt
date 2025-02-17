package com.adamdawi.popcornpicks.core.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class MoviesBasedOnGenreResponse(
    val page: Int,
    val results: List<RecommendedMovieByGenreDto>,
    val total_pages: Int,
    val total_results: Int
)