package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen

sealed interface MovieChooseEvent {
    data object Success: MovieChooseEvent
    data class Error(val error: String): MovieChooseEvent
}