package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

sealed interface GenresAction{
    data class ToggleGenreSelection(val genre: Genre): GenresAction
    object OnContinueClick: GenresAction
}