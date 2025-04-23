package com.adamdawi.popcornpicks.e2e.response

import com.adamdawi.popcornpicks.core.data.remote.response.MoviesBasedOnGenreResponse
import com.adamdawi.popcornpicks.core.data.remote.response.MoviesBasedOnMovieResponse
import com.adamdawi.popcornpicks.core.data.remote.response.RecommendedMovieByGenreDto
import com.adamdawi.popcornpicks.core.data.remote.response.RecommendedMovieByMovieDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun generateMockMoviesBasedOnMovieResponse(): String {
    val mockResponse = MoviesBasedOnMovieResponse(
        page = 1,
        results = listOf(
            RecommendedMovieByMovieDto(
                adult = false,
                backdrop_path = "/path/to/backdrop1",
                genre_ids = listOf(1, 2),
                id = 1,
                media_type = "movie",
                original_language = "en",
                original_title = "Fake Movie 1",
                overview = "Overview of Fake Movie 1",
                popularity = 10.0,
                poster_path = "/path/to/poster1",
                release_date = "2023-01-01",
                title = "Fake Movie 1",
                video = false,
                vote_average = 8.5,
                vote_count = 100
            )
        ),
        total_pages = 1,
        total_results = 1
    )
    return Json.encodeToString(mockResponse)
}

fun generateMockMoviesBasedOnGenreResponse(): String {
    val mockResponse = MoviesBasedOnGenreResponse(
        page = 1,
        results = listOf(
            RecommendedMovieByGenreDto(
                adult = false,
                backdrop_path = "/path/to/backdrop2",
                genre_ids = listOf(3, 4),
                id = 2,
                original_language = "en",
                original_title = "Genre Movie 1",
                overview = "Overview of Genre Movie 1",
                popularity = 9.0,
                poster_path = "/path/to/poster2",
                release_date = "2023-02-01",
                title = "Genre Movie 1",
                video = false,
                vote_average = 7.5,
                vote_count = 120
            ),
            RecommendedMovieByGenreDto(
                adult = false,
                backdrop_path = "/path/to/backdrop3",
                genre_ids = listOf(5, 6),
                id = 3,
                original_language = "en",
                original_title = "Genre Movie 2",
                overview = "Overview of Genre Movie 2",
                popularity = 8.5,
                poster_path = "/path/to/poster3",
                release_date = "2022-08-15",
                title = "Genre Movie 2",
                video = false,
                vote_average = 7.8,
                vote_count = 95
            ),
            RecommendedMovieByGenreDto(
                adult = false,
                backdrop_path = "/path/to/backdrop4",
                genre_ids = listOf(1, 2),
                id = 4,
                original_language = "en",
                original_title = "Genre Movie 3",
                overview = "Overview of Genre Movie 3",
                popularity = 10.2,
                poster_path = "/path/to/poster4",
                release_date = "2021-12-20",
                title = "Genre Movie 3",
                video = false,
                vote_average = 8.1,
                vote_count = 200
            ),
            RecommendedMovieByGenreDto(
                adult = false,
                backdrop_path = "/path/to/backdrop5",
                genre_ids = listOf(7, 8),
                id = 5,
                original_language = "en",
                original_title = "Genre Movie 4",
                overview = "Overview of Genre Movie 4",
                popularity = 7.3,
                poster_path = "/path/to/poster5",
                release_date = "2020-06-10",
                title = "Genre Movie 4",
                video = false,
                vote_average = 6.9,
                vote_count = 150
            ),
            RecommendedMovieByGenreDto(
                adult = false,
                backdrop_path = "/path/to/backdrop6",
                genre_ids = listOf(9, 10),
                id = 6,
                original_language = "en",
                original_title = "Genre Movie 5",
                overview = "Overview of Genre Movie 5",
                popularity = 9.5,
                poster_path = "/path/to/poster6",
                release_date = "2024-01-05",
                title = "Genre Movie 5",
                video = false,
                vote_average = 8.4,
                vote_count = 175
            )
        ),
        total_pages = 1,
        total_results = 5
    )
    return Json.encodeToString(mockResponse)
}
