package com.adamdawi.popcornpicks.core.domain.util

object Constants {
    object Network{
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"
    }
    object Local{
        const val SHARED_PREFERENCES_NAME = "popcorn_picks_preferences"
        val DEFAULT_GENRES_IDS = listOf("28", "12", "878")
    }
}