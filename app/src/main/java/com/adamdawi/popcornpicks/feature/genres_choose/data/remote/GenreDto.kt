package com.adamdawi.popcornpicks.feature.genres_choose.data.remote

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre
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