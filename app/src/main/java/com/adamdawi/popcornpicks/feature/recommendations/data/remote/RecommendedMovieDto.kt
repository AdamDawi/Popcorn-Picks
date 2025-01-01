package com.adamdawi.popcornpicks.feature.recommendations.data.remote

import com.adamdawi.popcornpicks.core.data.mapper.mapGenreIdsToGenre
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie
import kotlinx.serialization.Serializable

@Serializable
data class RecommendedMovieDto(
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

fun RecommendedMovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        poster = poster_path,
        releaseDate = release_date,
        voteAverage = vote_average,
        genres = mapGenreIdsToGenre(genre_ids)
    )
}