package com.adamdawi.popcornpicks.core.data.local

import android.content.SharedPreferences
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.feature.onboarding.domain.model.Genre

class GenresPreferencesImpl(
    private val sharedPreferences: SharedPreferences
): GenresPreferences{
    override fun saveGenres(genres: List<Genre>) {
        if(genres.isNotEmpty()){
            val genresStringList = genres.map { it.id.toString() }
            sharedPreferences.edit()
                .putStringSet(GENRES_KEY, genresStringList.toSet())
                .apply()
        }
    }

    override fun getGenres(): List<String> {
        return sharedPreferences.getStringSet(GENRES_KEY, emptySet())?.toList() ?: emptyList()
    }
    companion object {
        private const val GENRES_KEY = "genres_key"
    }
}