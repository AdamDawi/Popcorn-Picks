package com.adamdawi.popcornpicks.feature.recommendations.domain

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

data class RecommendedMovie(
    val id: Int,
    val title: String,
    val poster: String,
    val releaseDate: String,
    val voteAverage: Double,
    //TODO base on popularity display recommended movies
    val popularity: Double,
    val genres: List<Genre>
)

fun RecommendedMovie.formatMovieDetails(): String{
    val genres = this.genres.map { it.name }
    val genreText = genres.take(2).joinToString("/")
    return "${this.releaseDate.take(4)} · $genreText · ${this.voteAverage}/10"
}
