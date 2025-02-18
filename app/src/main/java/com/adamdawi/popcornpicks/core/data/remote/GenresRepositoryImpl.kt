package com.adamdawi.popcornpicks.core.data.remote

import com.adamdawi.popcornpicks.core.data.networking.get
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.util.map
import com.adamdawi.popcornpicks.core.data.remote.response.GenresResponse
import com.adamdawi.popcornpicks.core.data.remote.response.toGenre
import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.local.GenresRepository
import io.ktor.client.HttpClient

class GenresRepositoryImpl(
    private val httpClient: HttpClient
): GenresRepository {
    override suspend fun getGenres(): Result<List<Genre>, DataError.Network> {
        val result = httpClient.get<GenresResponse>(
            route = "genre/movie/list",
            queryParameters = mapOf("language" to "en")
        )

        return result.map {
            it.genres.map { genreDto -> genreDto.toGenre() }
        }
    }
}