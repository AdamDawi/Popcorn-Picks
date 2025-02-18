package com.adamdawi.popcornpicks.feature.recommendations.domain.repository

import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.EmptyResult
import com.adamdawi.popcornpicks.core.domain.util.Result

interface LocalMovieRecommendationsRepository {
    suspend fun getRecommendedMovies(): Result<List<Movie>, DataError.Local>
    suspend fun addRecommendedMovies(movies: List<Movie>): EmptyResult<DataError.Local>
    suspend fun deleteRecommendedMovie(movie: Movie): EmptyResult<DataError.Local>
}