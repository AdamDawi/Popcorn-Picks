package com.adamdawi.popcornpicks.feature.recommendations.presentation.components

import com.adamdawi.popcornpicks.feature.recommendations.domain.RecommendedMovie

fun RecommendedMovie.formatMovieDetails(): String{
    val genres = this.genres.map { it.name }
    val genreText = genres.take(2).joinToString("/")
    return "${this.releaseDate.take(4)} · $genreText · ${this.voteAverage}/10"
}