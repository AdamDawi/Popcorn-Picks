package com.adamdawi.popcornpicks.e2e

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
            )
        ),
        total_pages = 1,
        total_results = 1
    )
    return Json.encodeToString(mockResponse)
}