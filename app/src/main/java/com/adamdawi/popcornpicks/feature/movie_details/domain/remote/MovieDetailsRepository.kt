package com.adamdawi.popcornpicks.feature.movie_details.domain.remote

import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie

interface MovieDetailsRepository {
    suspend fun getDetailedMovie(movieId: String): Result<DetailedMovie, DataError.Network>
}