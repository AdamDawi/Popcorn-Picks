package com.adamdawi.popcornpicks.core.domain

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

interface GenresPreferences {
    fun saveGenres(genres: List<Genre>)
    fun getGenres(): List<String>
}