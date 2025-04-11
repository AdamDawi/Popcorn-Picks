package com.adamdawi.popcornpicks.core.domain.local

import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.EmptyResult
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.model.Movie

interface LikedMoviesDbRepository {
    suspend fun getLikedMovies(): Result<List<LikedMovie>, DataError.Local>
    suspend fun addLikedMovies(movies: List<Movie>): EmptyResult<DataError.Local>
    suspend fun addLikedMovie(movie: Movie): EmptyResult<DataError.Local>
    suspend fun updatePageForLikedMovie(movieId: Int, nextPage: Int): EmptyResult<DataError.Local>
    suspend fun updatePageForAllLikedMovies(nextPage: Int): EmptyResult<DataError.Local>
    suspend fun deleteLikedMovie(movie: Movie): EmptyResult<DataError.Local>
    suspend fun getLikedMoviesCount(): Result<Int?, DataError.Local>
}