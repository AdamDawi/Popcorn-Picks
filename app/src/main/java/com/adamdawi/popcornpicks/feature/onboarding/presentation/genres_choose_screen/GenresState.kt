package com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen

import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre

data class GenresState(
    val genres: List<Genre> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedGenres: List<Genre> = emptyList(),
    val continueButtonEnabled: Boolean = false
)