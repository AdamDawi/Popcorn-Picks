package com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen

sealed interface MovieDetailsAction {
    object OnBackClick : MovieDetailsAction
}