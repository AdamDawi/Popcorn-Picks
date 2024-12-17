package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie

sealed interface MovieChooseAction{
    data class ToggleMovieSelection(val movie: Movie): MovieChooseAction
    object OnFinishClick: MovieChooseAction
}