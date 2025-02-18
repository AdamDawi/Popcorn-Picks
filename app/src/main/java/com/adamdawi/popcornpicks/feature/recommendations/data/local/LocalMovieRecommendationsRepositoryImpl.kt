package com.adamdawi.popcornpicks.feature.recommendations.data.local

import com.adamdawi.popcornpicks.core.data.mapper.toRecommendedMovieEntity
import com.adamdawi.popcornpicks.core.data.utils.handleLocalError
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.EmptyResult
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.recommendations.data.local.dao.MovieRecommendationsDao
import com.adamdawi.popcornpicks.feature.recommendations.data.local.entity.toMovie
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.LocalMovieRecommendationsRepository

class LocalMovieRecommendationsRepositoryImpl(
    private val movieRecommendationsDao: MovieRecommendationsDao
): LocalMovieRecommendationsRepository {
    override suspend fun getRecommendedMovies(): Result<List<Movie>, DataError.Local> {
        return try{
            val movies = movieRecommendationsDao.getRecommendedMovies().map { it.toMovie() }
            Result.Success(movies)
        } catch (e: Exception){
            handleLocalError(e)
        }
    }

    override suspend fun addRecommendedMovies(movies: List<Movie>): EmptyResult<DataError.Local> {
        return try{
            movieRecommendationsDao.addRecommendedMovies(movies.map { it.toRecommendedMovieEntity() })
            Result.Success(Unit)
        }catch (e: Exception){
            handleLocalError(e)
        }
    }

    override suspend fun deleteRecommendedMovie(movie: Movie): EmptyResult<DataError.Local> {
        return try{
            movieRecommendationsDao.deleteRecommendedMovie(movie.toRecommendedMovieEntity())
            Result.Success(Unit)
        }catch (e: Exception) {
            handleLocalError(e)

        }
    }
}