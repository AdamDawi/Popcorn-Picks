package com.adamdawi.popcornpicks.feature.genres_choose.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val genres: List<GenreDto>
)