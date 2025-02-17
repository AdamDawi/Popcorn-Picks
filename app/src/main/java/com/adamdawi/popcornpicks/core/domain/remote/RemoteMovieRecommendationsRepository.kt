package com.adamdawi.popcornpicks.core.domain.remote

import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.model.Movie

interface RemoteMovieRecommendationsRepository {
    suspend fun getMoviesBasedOnMovie(movieId: Int, page: Int): Result<List<Movie>, DataError.Network>
    suspend fun getMoviesBasedOnGenre(genreId: String, page: Int): Result<List<Movie>, DataError.Network>
}