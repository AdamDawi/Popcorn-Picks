package com.adamdawi.popcornpicks.feature.genres_choose.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(
    val genres: List<GenreDto>
)