package com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen

import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie

data class MovieDetailsState(
    val movie: DetailedMovie = DetailedMovie(
        id = 0,
        title = "",
        poster = null,
        genres = emptyList(),
        overview = "",
        backdrop = null,
        releaseDate = "",
        voteAverage = 0.0,
        runtime = 0,
        popularity = 0.0
    ),
    val isLoading: Boolean = false,
    val error: String? = null
)