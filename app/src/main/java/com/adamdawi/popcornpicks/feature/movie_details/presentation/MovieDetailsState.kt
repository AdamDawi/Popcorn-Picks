package com.adamdawi.popcornpicks.feature.movie_details.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyDetailedMovie
import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie

data class MovieDetailsState(
    val movie: DetailedMovie = dummyDetailedMovie,
    val isLoading: Boolean = false,
    val error: String? = null
)