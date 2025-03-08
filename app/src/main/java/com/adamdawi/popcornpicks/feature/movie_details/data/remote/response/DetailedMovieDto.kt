package com.adamdawi.popcornpicks.feature.movie_details.data.remote.response

import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie
import com.adamdawi.popcornpicks.feature.onboarding.data.remote.response.GenreDto
import com.adamdawi.popcornpicks.feature.onboarding.data.remote.response.toGenre
import kotlinx.serialization.Serializable

@Serializable
data class DetailedMovieDto(
    val adult: Boolean?,
    val backdrop_path: String?,
    val budget: Int?,
    val genres: List<GenreDto>,
    val homepage: String?,
    val id: Int,
    val imdb_id: String?,
    val origin_country: List<String>?,
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val production_companies: List<ProductionCompany>?,
    val production_countries: List<ProductionCountry>?,
    val release_date: String?,
    val revenue: Long?,
    val runtime: Int?,
    val spoken_languages: List<SpokenLanguage>?,
    val status: String?,
    val tagline: String?,
    val title: String,
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?
)

@Serializable
data class ProductionCompany(
    val id: Int,
    val logo_path: String?,
    val name: String,
    val origin_country: String
)

@Serializable
data class ProductionCountry(
    val iso_3166_1: String,
    val name: String
)
@Serializable
data class SpokenLanguage(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)

fun DetailedMovieDto.toDetailedMovie(): DetailedMovie{
    return DetailedMovie(
        id = id,
        title = title,
        poster = poster_path,
        genres = genres.map { it.toGenre() },
        overview = overview,
        backdrop = backdrop_path,
        releaseDate = release_date,
        voteAverage = vote_average,
        runtime = runtime,
        popularity = popularity
    )
}