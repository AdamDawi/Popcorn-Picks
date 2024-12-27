package com.adamdawi.popcornpicks.core.data.dummy

import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie
import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie
import com.adamdawi.popcornpicks.feature.recommendations.domain.RecommendedMovie

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

val selectedGenres: List<Genre> = listOf(
    Genre(id = 36, name = "History"),
    Genre(id = 27, name = "Horror"),
    Genre(id = 10402, name = "Music")
)

val dummyMovie = Movie(
    id = 1,
    title = "Spiderman",
    poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
    releaseDate = "2020-04-02"
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
val dummyRecommendedMovie = RecommendedMovie(
    id = 1,
    title = "Spiderman",
    poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
    releaseDate = "2020-04-02",
    voteAverage = 7.6,
    genres = dummyGenresList
)

val dummyMovieList = List(20) { dummyMovie.copy(id = it) }

val selectedMovies: List<Movie> = listOf(
    dummyMovie.copy(id = 2),
    dummyMovie.copy(id = 6),
    dummyMovie.copy(id = 9)
)

val genreIdToNameMap = mapOf(
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