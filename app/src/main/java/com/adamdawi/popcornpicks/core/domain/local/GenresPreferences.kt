package com.adamdawi.popcornpicks.core.domain.local

import com.adamdawi.popcornpicks.core.domain.model.Genre

interface GenresPreferences {
    fun saveGenres(genres: List<Genre>)
    fun getGenres(): List<String>
    fun savePagesForGenres(pages: List<Int>)
    fun getGenresWithPage(): Map<String, Int>
}