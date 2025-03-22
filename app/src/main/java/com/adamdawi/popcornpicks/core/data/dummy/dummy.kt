package com.adamdawi.popcornpicks.core.data.dummy

import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie
import com.adamdawi.popcornpicks.R

val dummyGenresList = listOf(
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

val genreToDrawableMap = mapOf(
    "Action" to R.drawable.action,
    "Adventure" to R.drawable.adventure,
    "Animation" to R.drawable.animation,
    "Comedy" to R.drawable.comedy,
    "Crime" to R.drawable.crime,
    "Documentary" to R.drawable.documentary,
    "Drama" to R.drawable.drama,
    "Family" to R.drawable.family,
    "Fantasy" to R.drawable.fantasy,
    "History" to R.drawable.history,
    "Horror" to R.drawable.horror,
    "Music" to R.drawable.music,
    "Mystery" to R.drawable.mystery,
    "Romance" to R.drawable.romance,
    "Science Fiction" to R.drawable.science_fiction,
    "TV Movie" to R.drawable.tv_movie,
    "Thriller" to R.drawable.thriller,
    "War" to R.drawable.war,
    "Western" to R.drawable.western
)

val profileImages = listOf(
    R.drawable.happy_guard,
    R.drawable.sunglasses_guard,
    R.drawable.policeman,
    R.drawable.check_in,
    R.drawable.guard_forbidding_to_pass,
    R.drawable.character_in_armor,
    R.drawable.monster_security_shield,
    R.drawable.security,
    R.drawable.security_with_micro,
    R.drawable.security_without_face,
)

val selectedGenres: List<Genre> = listOf(
    Genre(id = 36, name = "History"),
    Genre(id = 27, name = "Horror"),
    Genre(id = 10402, name = "Music")
)

val dummyMovie = Movie(
    id = 1,
    title = "Spiderman",
    poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
    releaseDate = "2020-04-02",
    voteAverage = 7.6,
    genres = dummyGenresList
)

val dummyDetailedMovie = DetailedMovie(
    id = 1,
    title = "Spider-Man: No Way Home",
    poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
    genres = listOf(
        Genre(id = 28, name = "Action"),
        Genre(id = 12, name = "Adventure"),
        Genre(id = 878, name = "Science Fiction")
    ),
    overview = "Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.",
    backdrop = "/zD5v1E4joAzFvmAEytt7fM3ivyT.jpg",
    releaseDate = "2021-12-15",
    voteAverage = 7.6,
    runtime = 148,
    popularity = 181.095
)

val dummyMovieList = List(20) { dummyMovie.copy(id = it) }

val selectedMovies: List<Movie> = listOf(
    dummyMovie.copy(id = 2),
    dummyMovie.copy(id = 6),
    dummyMovie.copy(id = 9)
)