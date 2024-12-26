package com.adamdawi.popcornpicks.feature.onboarding.domain

import com.adamdawi.popcornpicks.core.domain.model.MovieEntity

data class Movie(
    val id: Int,
    val title: String,
    val poster: String?,
    val releaseDate: String
)

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        poster = poster,
        releaseDate = releaseDate
    )
}