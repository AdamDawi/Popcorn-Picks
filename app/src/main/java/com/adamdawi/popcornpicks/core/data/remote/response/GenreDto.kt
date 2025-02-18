package com.adamdawi.popcornpicks.core.data.remote.response

import com.adamdawi.popcornpicks.core.domain.model.Genre
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)

fun GenreDto.toGenre(): Genre {
    return Genre(
        id = id,
        name = name
    )
}