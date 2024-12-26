package com.adamdawi.popcornpicks.feature.onboarding.data.repository

import com.adamdawi.popcornpicks.core.data.networking.get
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.util.map
import com.adamdawi.popcornpicks.feature.onboarding.data.remote.GenresResponse
import com.adamdawi.popcornpicks.feature.onboarding.data.remote.toGenre
import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.GenresRepository
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