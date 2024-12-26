package com.adamdawi.popcornpicks.core.domain.local

import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre

interface GenresPreferences {
    fun saveGenres(genres: List<Genre>)
    fun getGenres(): List<String>
}