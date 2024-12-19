package com.adamdawi.popcornpicks.core.data.local

import com.adamdawi.popcornpicks.core.domain.model.toMovie
import com.adamdawi.popcornpicks.core.domain.repository.MoviesDbRepository
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import com.adamdawi.popcornpicks.feature.movie_choose.domain.toMovieEntity

class MoviesDbRepositoryImpl(
    private val moviesDao: MoviesDao
): MoviesDbRepository {
    override suspend fun getMovies(): List<Movie> {
        return moviesDao.getMovies().map { it.toMovie() }
    }

    override suspend fun addMovies(movies: List<Movie>) {
        moviesDao.addMovies(movies.map { it.toMovieEntity() })
    }

    override suspend fun addMovie(movie: Movie) {
        moviesDao.addMovie(movie.toMovieEntity())
    }

    override suspend fun deleteMovie(movie: Movie) {
        moviesDao.deleteMovie(movie.toMovieEntity())
    }
}