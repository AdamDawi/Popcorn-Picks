package com.adamdawi.popcornpicks.feature.onboarding.domain.repository

import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie

interface MovieChooseRepository {
    suspend fun getMovies(genreId: String, page: Int): Result<List<Movie>, DataError.Network>
}