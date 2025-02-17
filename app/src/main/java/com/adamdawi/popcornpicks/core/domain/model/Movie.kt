package com.adamdawi.popcornpicks.core.domain.model

import com.adamdawi.popcornpicks.core.data.local.entity.LikedMovieEntity
import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre

data class Movie(
    val id: Int,
    val title: String,
    val poster: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val genres: List<Genre>
)

fun Movie.toMovieEntity(): LikedMovieEntity {
    return LikedMovieEntity(
        id = id,
        title = title,
        poster = poster,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        genresIds = genres.map { it.id }
    )
}