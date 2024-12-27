package com.adamdawi.popcornpicks.feature.onboarding.data.repository

import com.adamdawi.popcornpicks.core.data.networking.get
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.util.map
import com.adamdawi.popcornpicks.feature.onboarding.data.remote.MoviesBasedOnGenreResponse
import com.adamdawi.popcornpicks.feature.onboarding.data.remote.toMovie
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.MoviesByGenreRepository
import io.ktor.client.HttpClient

class MoviesByGenreRepositoryImpl(
    private val httpClient: HttpClient
): MoviesByGenreRepository {
    override suspend fun getMoviesBasedOnGenre(genreId: String, page: Int): Result<List<Movie>, DataError.Network> {
        val result = httpClient.get<MoviesBasedOnGenreResponse>(
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
