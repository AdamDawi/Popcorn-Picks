package com.adamdawi.popcornpicks.core.data.local

import com.adamdawi.popcornpicks.core.domain.model.toMovie
import com.adamdawi.popcornpicks.core.domain.repository.MoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.EmptyResult
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import com.adamdawi.popcornpicks.feature.movie_choose.domain.toMovieEntity

class MoviesDbRepositoryImpl(
    private val moviesDao: MoviesDao
): MoviesDbRepository {
    override suspend fun getMovies(): Result<List<Movie>, DataError.Local> {
        return try {
            val movies = moviesDao.getMovies().map { it.toMovie() }
            Result.Success(movies)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    override suspend fun addMovies(movies: List<Movie>): EmptyResult<DataError.Local> {
        return try {
            moviesDao.addMovies(movies.map { it.toMovieEntity() })
            Result.Success(Unit)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    override suspend fun addMovie(movie: Movie): EmptyResult<DataError.Local> {
        return try {
            moviesDao.addMovie(movie.toMovieEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    override suspend fun deleteMovie(movie: Movie): EmptyResult<DataError.Local> {
        return try {
            moviesDao.deleteMovie(movie.toMovieEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    private fun <T> handleLocalError(e: Exception): Result<T, DataError.Local> {
        return when (e) {
            is java.io.IOException -> Result.Error(DataError.Local.DISK_FULL)
            else -> Result.Error(DataError.Local.UNKNOWN)
        }
    }
}