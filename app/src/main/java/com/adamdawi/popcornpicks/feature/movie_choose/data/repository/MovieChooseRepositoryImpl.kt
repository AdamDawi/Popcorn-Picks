package com.adamdawi.popcornpicks.feature.movie_choose.data.repository

import com.adamdawi.popcornpicks.core.data.networking.get
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.util.map
import com.adamdawi.popcornpicks.feature.movie_choose.data.remote.MovieChooseResponse
import com.adamdawi.popcornpicks.feature.movie_choose.data.remote.toMovie
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import com.adamdawi.popcornpicks.feature.movie_choose.domain.repository.MovieChooseRepository
import io.ktor.client.HttpClient

class MovieChooseRepositoryImpl(
    private val httpClient: HttpClient
): MovieChooseRepository {
    override suspend fun getMovies(genreId: String, page: Int): Result<List<Movie>, DataError.Network> {
        val result = httpClient.get<MovieChooseResponse>(
            route = "discover/movie",
            queryParameters = mapOf(
                "include_adult" to "false",
                "include_video" to "false",
                "language" to "en-US",
                "page" to page.toString(),
                "sort_by" to "revenue.desc",
                "with_genres" to genreId
            )
        )

        return result.map {
            it.results.map { movieDto -> movieDto.toMovie() }
        }
    }
}
