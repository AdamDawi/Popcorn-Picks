package com.adamdawi.popcornpicks.core.data.remote.response

import com.adamdawi.popcornpicks.core.data.mapper.mapGenreIdsToGenre
import com.adamdawi.popcornpicks.core.domain.model.Movie
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedMovieByMovieDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val media_type: String,
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

fun RecommendedMovieByMovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        poster = poster_path,
        releaseDate = release_date,
        voteAverage = vote_average,
        genres = mapGenreIdsToGenre(genre_ids)
    )
}