package com.adamdawi.popcornpicks.feature.genres_choose.data.repository

import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre
import com.adamdawi.popcornpicks.feature.genres_choose.domain.repository.GenresRepository

class GenresRepositoryImpl: GenresRepository {
    override fun getGenres(): List<Genre> {
        return dummyGenresList
    }
}