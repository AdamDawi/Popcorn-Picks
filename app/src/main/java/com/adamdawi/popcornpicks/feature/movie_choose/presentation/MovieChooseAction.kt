package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie

sealed interface MovieChooseAction{
    data class SelectMovie(val movie: Movie): MovieChooseAction
    object OnFinishClick: MovieChooseAction
}