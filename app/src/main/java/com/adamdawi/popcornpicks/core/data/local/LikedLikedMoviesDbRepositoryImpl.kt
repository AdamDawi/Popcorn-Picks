package com.adamdawi.popcornpicks.core.data.local

import com.adamdawi.popcornpicks.core.data.local.entity.toMovie
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.EmptyResult
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.model.toMovieEntity

class LikedLikedMoviesDbRepositoryImpl(
    private val likedMoviesDao: LikedMoviesDao
): LikedMoviesDbRepository {
    override suspend fun getLikedMovies(): Result<List<Movie>, DataError.Local> {
        return try {
            val movies = likedMoviesDao.getLikedMovies().map { it.toMovie() }
            Result.Success(movies)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    override suspend fun addLikedMovies(movies: List<Movie>): EmptyResult<DataError.Local> {
        return try {
            likedMoviesDao.addLikedMovies(movies.map { it.toMovieEntity() })
            Result.Success(Unit)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    override suspend fun addLikedMovie(movie: Movie): EmptyResult<DataError.Local> {
        return try {
            likedMoviesDao.addLikedMovie(movie.toMovieEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    override suspend fun deleteLikedMovie(movie: Movie): EmptyResult<DataError.Local> {
        return try {
            likedMoviesDao.deleteLikedMovie(movie.toMovieEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    private fun <T> handleLocalError(e: Exception): Result<T, DataError.Local> {
        e.printStackTrace()
        return when (e) {
            is java.io.IOException -> Result.Error(DataError.Local.DISK_FULL)
            else -> Result.Error(DataError.Local.UNKNOWN)
        }
    }
}