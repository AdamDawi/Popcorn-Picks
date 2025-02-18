package com.adamdawi.popcornpicks.core.domain.local

import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.domain.model.Genre

interface GenresRepository {
    suspend fun getGenres(): Result<List<Genre>, DataError.Network>
}