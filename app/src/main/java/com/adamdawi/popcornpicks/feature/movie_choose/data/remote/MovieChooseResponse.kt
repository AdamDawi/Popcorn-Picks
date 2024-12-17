package com.adamdawi.popcornpicks.feature.movie_choose.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MovieChooseResponse(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)