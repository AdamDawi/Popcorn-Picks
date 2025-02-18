package com.adamdawi.popcornpicks.feature.onboarding.data.remote

import com.adamdawi.popcornpicks.feature.onboarding.domain.model.Genre
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