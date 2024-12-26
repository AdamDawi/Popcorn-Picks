package com.adamdawi.popcornpicks.core.domain.repository

import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.EmptyResult
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie

interface MoviesDbRepository {
    suspend fun getMovies(): Result<List<Movie>, DataError.Local>
    suspend fun addMovies(movies: List<Movie>): EmptyResult<DataError.Local>
    suspend fun addMovie(movie: Movie): EmptyResult<DataError.Local>
    suspend fun deleteMovie(movie: Movie): EmptyResult<DataError.Local>
}