package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.utils

import com.adamdawi.popcornpicks.core.domain.model.Movie
import java.util.Locale

fun Movie.formatMovieDetails(): String {
    val genres = this.genres.map { it.name }
    val genreText = if (genres.isEmpty()) "???" else genres.take(2).joinToString("/")
    val releaseDateText = if (this.releaseDate.isEmpty()) "???" else this.releaseDate.take(4)
    val voteAverageText = if (voteAverage==0.0) "???" else "${"%.1f".format(Locale.US, voteAverage)}/10"

    return "$releaseDateText · $genreText · $voteAverageText"
}