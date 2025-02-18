package com.adamdawi.popcornpicks.core.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class GenresResponse(
    val genres: List<GenreDto>
)