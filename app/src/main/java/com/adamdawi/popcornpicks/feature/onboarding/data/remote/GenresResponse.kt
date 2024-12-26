package com.adamdawi.popcornpicks.feature.onboarding.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(
    val genres: List<GenreDto>
)