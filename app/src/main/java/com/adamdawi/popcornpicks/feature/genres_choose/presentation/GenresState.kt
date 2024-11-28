package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

data class GenresState(
    val genres: List<Genre> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedGenres: List<Boolean> = emptyList()
)