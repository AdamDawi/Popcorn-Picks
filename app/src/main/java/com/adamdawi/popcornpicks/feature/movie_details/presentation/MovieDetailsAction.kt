package com.adamdawi.popcornpicks.feature.movie_details.presentation

sealed interface MovieDetailsAction {
    object OnBackClick : MovieDetailsAction
}