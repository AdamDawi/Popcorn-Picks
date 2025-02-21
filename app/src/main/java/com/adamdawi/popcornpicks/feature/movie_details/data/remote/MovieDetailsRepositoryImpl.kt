package com.adamdawi.popcornpicks.feature.movie_details.data.remote

import com.adamdawi.popcornpicks.core.data.networking.get
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.util.map
import com.adamdawi.popcornpicks.feature.movie_details.data.remote.response.DetailedMovieDto
import com.adamdawi.popcornpicks.feature.movie_details.data.remote.response.toDetailedMovie
import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie
import com.adamdawi.popcornpicks.feature.movie_details.domain.remote.MovieDetailsRepository
import io.ktor.client.HttpClient

class MovieDetailsRepositoryImpl(
    private val httpClient: HttpClient
): MovieDetailsRepository {
    override suspend fun getDetailedMovie(movieId: String): Result<DetailedMovie, DataError.Network> {
        val result = httpClient.get<DetailedMovieDto>(
            route = "movie/$movieId",
            queryParameters = mapOf(
                "language" to "en-US",
            )
        )
        return result.map {
            it.toDetailedMovie()
        }
    }
}