package com.adamdawi.popcornpicks.core.data.local

import com.adamdawi.popcornpicks.core.data.local.dao.LikedMoviesDao
import com.adamdawi.popcornpicks.core.data.local.entity.toLikedMovie
import com.adamdawi.popcornpicks.core.data.mapper.toLikedMovieEntity
import com.adamdawi.popcornpicks.core.data.utils.handleLocalError
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.EmptyResult
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.model.Movie

class LikedLikedMoviesDbRepositoryImpl(
    private val likedMoviesDao: LikedMoviesDao
): LikedMoviesDbRepository {
    override suspend fun getLikedMovies(): Result<List<LikedMovie>, DataError.Local> {
        return try {
            val movies = likedMoviesDao.getLikedMovies().map { it.toLikedMovie() }
            Result.Success(movies)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    override suspend fun addLikedMovies(movies: List<Movie>): EmptyResult<DataError.Local> {
        return try {
            likedMoviesDao.addLikedMovies(movies.map { it.toLikedMovieEntity() })
            Result.Success(Unit)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    override suspend fun addLikedMovie(movie: Movie): EmptyResult<DataError.Local> {
        return try {
            likedMoviesDao.addLikedMovie(movie.toLikedMovieEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }

    override suspend fun updatePageForLikedMovie(
        movieId: Int,
        nextPage: Int
    ): EmptyResult<DataError.Local> {
        return try {
            likedMoviesDao.updatePageForLikedMovie(movieId, nextPage)
            Result.Success(Unit)
        }catch (e: Exception){
            handleLocalError(e)
        }
    }

    override suspend fun deleteLikedMovie(movie: Movie): EmptyResult<DataError.Local> {
        return try {
            likedMoviesDao.deleteLikedMovie(movie.toLikedMovieEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            handleLocalError(e)
        }
    }
}