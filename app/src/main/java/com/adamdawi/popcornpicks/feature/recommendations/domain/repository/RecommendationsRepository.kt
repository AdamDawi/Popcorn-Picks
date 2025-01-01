package com.adamdawi.popcornpicks.feature.recommendations.domain.repository

import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie

interface RecommendationsRepository {
    suspend fun getMoviesBasedOnMovie(movieId: Int, page: Int): Result<List<Movie>, DataError.Network>
}