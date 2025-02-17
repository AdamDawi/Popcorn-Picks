package com.adamdawi.popcornpicks.core.data.remote

import com.adamdawi.popcornpicks.core.data.networking.get
import com.adamdawi.popcornpicks.core.data.remote.response.MoviesBasedOnMovieResponse
import com.adamdawi.popcornpicks.core.data.remote.response.toMovie
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.util.map
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.core.data.remote.response.MoviesBasedOnGenreResponse
import io.ktor.client.HttpClient

class RemoteMovieRecommendationsRepositoryImpl(
    private val httpClient: HttpClient
): RemoteMovieRecommendationsRepository {
    override suspend fun getMoviesBasedOnMovie(
        movieId: Int,
        page: Int
    ): Result<List<Movie>, DataError.Network> {
        val result = httpClient.get<MoviesBasedOnMovieResponse>(
            route = "movie/$movieId/recommendations",
            queryParameters = mapOf(
                "language" to "en-US",
                "page" to page.toString(),
            )
        )
        return result.map {
            it.results.map { recommendedMovieDto ->
                recommendedMovieDto.toMovie()
            }
        }
    }
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