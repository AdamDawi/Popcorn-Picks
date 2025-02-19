package com.adamdawi.popcornpicks.core.data.local

import android.content.SharedPreferences
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.model.Genre

class GenresPreferencesImpl(
    private val sharedPreferences: SharedPreferences
): GenresPreferences{
    override fun saveGenres(genres: List<Genre>) {
        if(genres.isNotEmpty()){
            val genresString = genres.joinToString(",") { it.id.toString() }
            sharedPreferences.edit()
                .putString(GENRES_KEY, genresString)
                .apply()
        }
    }

    override fun getGenres(): List<String> {
        return sharedPreferences.getString(GENRES_KEY, "")
            ?.split(",")
            ?.filter { it.isNotEmpty() }
            ?: emptyList()
    }

    override fun savePagesForGenres(pages: List<Int>) {
        if(pages.isNotEmpty()){
            val pagesString = pages.joinToString(",")
            sharedPreferences.edit()
                .putString(GENRE_PAGES_KEY, pagesString)
                .apply()
        }
    }

    override fun getGenresWithPage(): Map<String, Int> {
        val genres = getGenres()
        val pagesString = sharedPreferences.getString(GENRE_PAGES_KEY, "") ?: ""
        val pagesList = pagesString.split(",").mapNotNull { it.toIntOrNull() }

        return genres.zip(pagesList).toMap()
    }

    companion object {
        private const val GENRES_KEY = "genres_key"
        private const val GENRE_PAGES_KEY = "genre_pages_key"
    }
}