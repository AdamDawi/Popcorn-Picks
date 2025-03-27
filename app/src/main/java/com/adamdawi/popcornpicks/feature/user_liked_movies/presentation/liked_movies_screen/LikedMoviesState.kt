package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen

import com.adamdawi.popcornpicks.core.data.dummy.dummyLikedMoviesList
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie

data class LikedMoviesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val movies: List<LikedMovie> = dummyLikedMoviesList
)
