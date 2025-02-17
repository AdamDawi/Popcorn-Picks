package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen

import com.adamdawi.popcornpicks.core.domain.model.Movie

sealed interface MovieChooseAction{
    data class ToggleMovieSelection(val movie: Movie): MovieChooseAction
    object OnFinishClick: MovieChooseAction
}