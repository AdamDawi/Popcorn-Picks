package com.adamdawi.popcornpicks.e2e.response

import com.adamdawi.popcornpicks.feature.movie_details.data.remote.response.DetailedMovieDto
import com.adamdawi.popcornpicks.feature.movie_details.data.remote.response.ProductionCompany
import com.adamdawi.popcornpicks.feature.movie_details.data.remote.response.ProductionCountry
import com.adamdawi.popcornpicks.feature.movie_details.data.remote.response.SpokenLanguage
import com.adamdawi.popcornpicks.feature.onboarding.data.remote.response.GenreDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun generateMockDetailedMovieId1Response(): String {
    val mockResponse = DetailedMovieDto(
        adult = false,
        backdrop_path = "/path/to/backdrop.jpg",
        budget = 150000000,
        genres = listOf(
            GenreDto(id = 16, name = "Animation"),
            GenreDto(id = 12, name = "Adventure")
        ),
        homepage = "https://www.fakemovie.com",
        id = 1,
        imdb_id = "tt1234567",
        origin_country = listOf("US"),
        original_language = "en",
        original_title = "Fake Original Title",
        overview = "Overview of Fake Movie 1",
        popularity = 88.8,
        poster_path = "/path/to/poster.jpg",
        production_companies = listOf(
            ProductionCompany(
                id = 1,
                logo_path = "/path/to/logo.png",
                name = "Fake Studio",
                origin_country = "US"
            )
        ),
        production_countries = listOf(
            ProductionCountry(
                iso_3166_1 = "US",
                name = "United States of America"
            )
        ),
        release_date = "2023-01-01",
        revenue = 500000000,
        runtime = 120,
        spoken_languages = listOf(
            SpokenLanguage(
                english_name = "English",
                iso_639_1 = "en",
                name = "English"
            )
        ),
        status = "Released",
        tagline = "The fake adventure begins.",
        title = "Fake Movie 1",
        video = false,
        vote_average = 8.5,
        vote_count = 9876
    )
    return Json.encodeToString(mockResponse)
}

fun generateMockDetailedMovieResponse(): String {
    val mockResponse = DetailedMovieDto(
        adult = false,
        backdrop_path = "/path/to/another_backdrop.jpg",
        budget = 95000000,
        genres = listOf(
            GenreDto(id = 35, name = "Comedy"),
            GenreDto(id = 18, name = "Drama")
        ),
        homepage = "https://www.mockcomedy.com",
        id = 1,
        imdb_id = "tt0000001",
        origin_country = listOf("GB"),
        original_language = "en",
        original_title = "Mock Comedy Original",
        overview = "A hilarious and touching story about friendship and growth.",
        popularity = 72.3,
        poster_path = "/path/to/another_poster.jpg",
        production_companies = listOf(
            ProductionCompany(
                id = 2,
                logo_path = "/path/to/another_logo.png",
                name = "Mock Films Ltd.",
                origin_country = "GB"
            )
        ),
        production_countries = listOf(
            ProductionCountry(
                iso_3166_1 = "GB",
                name = "United Kingdom"
            )
        ),
        release_date = "2022-05-20",
        revenue = 200000000,
        runtime = 105,
        spoken_languages = listOf(
            SpokenLanguage(
                english_name = "English",
                iso_639_1 = "en",
                name = "English"
            )
        ),
        status = "Released",
        tagline = "Laughs. Tears. Life.",
        title = "Mock Comedy Movie",
        video = false,
        vote_average = 8.1,
        vote_count = 4567
    )
    return Json.encodeToString(mockResponse)
}
