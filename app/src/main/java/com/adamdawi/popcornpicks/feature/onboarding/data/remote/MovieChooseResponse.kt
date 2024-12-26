package com.adamdawi.popcornpicks.feature.onboarding.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class MovieChooseResponse(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)