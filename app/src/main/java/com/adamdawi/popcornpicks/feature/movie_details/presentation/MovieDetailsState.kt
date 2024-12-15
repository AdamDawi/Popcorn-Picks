package com.adamdawi.popcornpicks.feature.movie_details.presentation

import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie

data class MovieDetailsState(
    val movie: DetailedMovie? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)