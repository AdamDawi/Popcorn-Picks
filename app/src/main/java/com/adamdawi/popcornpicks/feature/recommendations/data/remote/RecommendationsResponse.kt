package com.adamdawi.popcornpicks.feature.recommendations.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RecommendationsResponse(
    val page: Int,
    val results: List<RecommendedMovieDto>,
    val total_pages: Int,
    val total_results: Int
)