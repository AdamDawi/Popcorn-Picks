package com.adamdawi.popcornpicks.core.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class MoviesBasedOnMovieResponse(
    val page: Int,
    val results: List<RecommendedMovieByMovieDto>,
    val total_pages: Int,
    val total_results: Int
)