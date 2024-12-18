package com.adamdawi.popcornpicks.feature.movie_choose.data.remote

import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        poster = poster_path,
        releaseDate = release_date
    )
}