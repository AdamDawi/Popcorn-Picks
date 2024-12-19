package com.adamdawi.popcornpicks.core.domain

import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import kotlinx.coroutines.flow.Flow

interface MyPicksDbRepository {
    suspend fun observeMovies(): Flow<List<Movie>>
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
}