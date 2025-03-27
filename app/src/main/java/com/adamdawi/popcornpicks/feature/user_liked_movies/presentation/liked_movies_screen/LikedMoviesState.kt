package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen

data class LikedMoviesState(
    val isLoading: Boolean = false,
    val error: String? = null
)
