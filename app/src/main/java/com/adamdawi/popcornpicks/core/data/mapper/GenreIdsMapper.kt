package com.adamdawi.popcornpicks.core.data.mapper

import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre

fun mapGenreIdsToGenre(genreIds: List<Int>): List<Genre> {
    return genreIds.map { genreId ->
        Genre(
            id = genreId,
            name = genreIdToNameMap[genreId] ?: "Unknown"
        )
    }
}

private val genreIdToNameMap = mapOf(
    28 to "Action",
    12 to "Adventure",
    16 to "Animation",
    35 to "Comedy",
    80 to "Crime",
    99 to "Documentary",
    18 to "Drama",
    10751 to "Family",
    14 to "Fantasy",
    36 to "History",
    27 to "Horror",
    10402 to "Music",
    9648 to "Mystery",
    10749 to "Romance",
    878 to "Science Fiction",
    10770 to "TV Movie",
    53 to "Thriller",
    10752 to "War",
    37 to "Western"
)