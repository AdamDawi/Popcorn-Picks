package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

data class GenresState(
    //TODO change this state values
    val genres: List<Genre> = dummyGenresList,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedGenres: List<Boolean> = com.adamdawi.popcornpicks.core.data.dummy.selectedGenres
)