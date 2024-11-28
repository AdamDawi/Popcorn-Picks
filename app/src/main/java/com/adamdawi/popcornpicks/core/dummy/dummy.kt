package com.adamdawi.popcornpicks.core.dummy

import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre

val dummyGenres = listOf(
    Genre(id = 28, name = "Action"),
    Genre(id = 12, name = "Adventure"),
    Genre(id = 16, name = "Animation"),
    Genre(id = 35, name = "Comedy"),
    Genre(id = 80, name = "Crime"),
    Genre(id = 99, name = "Documentary"),
    Genre(id = 18, name = "Drama"),
    Genre(id = 10751, name = "Family"),
    Genre(id = 14, name = "Fantasy"),
    Genre(id = 36, name = "History"),
    Genre(id = 27, name = "Horror"),
    Genre(id = 10402, name = "Music"),
    Genre(id = 9648, name = "Mystery"),
    Genre(id = 10749, name = "Romance"),
    Genre(id = 878, name = "Science Fiction"),
    Genre(id = 10770, name = "TV Movie"),
    Genre(id = 53, name = "Thriller"),
    Genre(id = 10752, name = "War"),
    Genre(id = 37, name = "Western")
)

val selectedGenres: List<Boolean> = List(dummyGenres.size) { it == 1 || it == 3 || it == 8 }
