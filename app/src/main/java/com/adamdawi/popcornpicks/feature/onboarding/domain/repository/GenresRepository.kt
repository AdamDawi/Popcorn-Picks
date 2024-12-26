package com.adamdawi.popcornpicks.feature.onboarding.domain.repository

import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre

interface GenresRepository {
    suspend fun getGenres(): Result<List<Genre>, DataError.Network>
}