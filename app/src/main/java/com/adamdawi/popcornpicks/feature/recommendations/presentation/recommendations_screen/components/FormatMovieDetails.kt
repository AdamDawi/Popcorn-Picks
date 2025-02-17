package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.components

import com.adamdawi.popcornpicks.core.domain.model.Movie

fun Movie.formatMovieDetails(): String {
    val genres = this.genres.map { it.name }
    val genreText = if (genres.isEmpty()) "???" else genres.take(2).joinToString("/")
    val releaseDateText = if (this.releaseDate.isEmpty()) "???" else this.releaseDate.take(4)

    return "$releaseDateText · $genreText · ${this.voteAverage}/10"
}