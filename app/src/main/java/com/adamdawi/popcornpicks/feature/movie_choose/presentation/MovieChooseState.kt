package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie

data class MovieChooseState(
    //TODO change this state values
    val movies: List<Movie> = dummyMovieList,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedMovies: List<Boolean> = com.adamdawi.popcornpicks.core.data.dummy.selectedMovies
)