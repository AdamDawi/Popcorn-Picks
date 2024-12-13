package com.adamdawi.popcornpicks.feature.genres_choose.domain.repository

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

interface GenresRepository {
    fun getGenres(): List<Genre>
}