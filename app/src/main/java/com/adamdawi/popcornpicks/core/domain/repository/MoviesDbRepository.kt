package com.adamdawi.popcornpicks.core.domain.repository

import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie

interface MoviesDbRepository {
    suspend fun getMovies(): List<Movie>
    suspend fun addMovies(movies: List<Movie>)
    suspend fun addMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
}