package com.adamdawi.popcornpicks.feature.recommendations.data.repository

import com.adamdawi.popcornpicks.core.data.networking.get
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.util.map
import com.adamdawi.popcornpicks.feature.recommendations.data.remote.RecommendationsResponse
import com.adamdawi.popcornpicks.feature.recommendations.data.remote.toRecommendedMovie
import com.adamdawi.popcornpicks.feature.recommendations.domain.RecommendedMovie
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.RecommendationsRepository
import io.ktor.client.HttpClient

class RecommendationsRepositoryImpl(
    private val httpClient: HttpClient
): RecommendationsRepository {
    override suspend fun getMoviesBasedOnMovie(
        movieId: Int,
        page: Int
    ): Result<List<RecommendedMovie>, DataError.Network> {
        val result = httpClient.get<RecommendationsResponse>(
            route = "movie/$movieId/recommendations",
            queryParameters = mapOf(
                "language" to "en-US",
                "page" to page.toString(),
            )
        )
        return result.map {
            it.results.map { recommendedMovieDto ->
                recommendedMovieDto.toRecommendedMovie()
            }
        }
    }
}