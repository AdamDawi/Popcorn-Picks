package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen

sealed interface LikedMoviesAction {
    data object OnBackClicked: LikedMoviesAction
}