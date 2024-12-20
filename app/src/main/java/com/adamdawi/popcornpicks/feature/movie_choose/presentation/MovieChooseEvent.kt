package com.adamdawi.popcornpicks.feature.movie_choose.presentation

sealed interface MovieChooseEvent {
    data object Success: MovieChooseEvent
    data class Error(val error: String): MovieChooseEvent
}