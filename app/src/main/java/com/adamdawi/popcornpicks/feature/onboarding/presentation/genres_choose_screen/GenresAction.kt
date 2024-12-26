package com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen

import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre

sealed interface GenresAction{
    data class ToggleGenreSelection(val genre: Genre): GenresAction
    object OnContinueClick: GenresAction
}