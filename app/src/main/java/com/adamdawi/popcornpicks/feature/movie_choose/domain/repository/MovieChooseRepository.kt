package com.adamdawi.popcornpicks.feature.movie_choose.domain.repository

import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie

interface MovieChooseRepository {
    suspend fun getMovies(genresIds: List<String>): Result<List<Movie>, DataError.Network>
}