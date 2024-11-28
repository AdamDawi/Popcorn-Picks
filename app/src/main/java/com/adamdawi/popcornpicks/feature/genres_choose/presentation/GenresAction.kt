package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

sealed interface GenresAction{
    data class SelectGenre(val genre: Genre): GenresAction
    data class OnContinueClick(val genres: List<Genre>): GenresAction
}