package com.adamdawi.popcornpicks.feature.recommendations.presentation.components

import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.components.formatMovieDetails
import junit.framework.TestCase.assertEquals
import org.junit.Test

class FormatMovieDetailsTest{

    @Test
    fun formatMovieDetails_singleGenre_shouldReturnFormattedStringWithSingleGenre() {
        //Arrange
        val movie = Movie(
            id = 1,
            title = "Action Movie",
            poster = null,
            releaseDate = "2025-12-10",
            voteAverage = 8.2,
            genres = listOf(Genre(1, "Action"))
        )

        //Act
        val formattedDetails = movie.formatMovieDetails()

        //Assert
        assertEquals("2025 · Action · 8.2/10", formattedDetails)
    }

    @Test
    fun formatMovieDetails_twoGenres_shouldReturnFormattedStringWithTwoGenres() {
        //Arrange
        val movie = Movie(
            id = 1,
            title = "Action Comedy Movie",
            poster = null,
            releaseDate = "2025-12-10",
            voteAverage = 8.2,
            genres = listOf(Genre(1, "Action"), Genre(3, "Comedy"))
        )

        //Act
        val formattedDetails = movie.formatMovieDetails()

        //Assert
        assertEquals("2025 · Action/Comedy · 8.2/10", formattedDetails)
    }

    @Test
    fun formatMovieDetails_moreThanTwoGenres_shouldReturnFormattedStringWithFirstTwoGenres() {
        //Arrange
        val movie = Movie(
            id = 1,
            title = "Action Comedy Drama Movie",
            poster = null,
            releaseDate = "2025-12-10",
            voteAverage = 8.2,
            genres = listOf(Genre(1, "Action"), Genre(3, "Comedy"), Genre(8,"Drama"))
        )

        //Act
        val formattedDetails = movie.formatMovieDetails()

        //Assert
        assertEquals("2025 · Action/Comedy · 8.2/10", formattedDetails)
    }

    @Test
    fun formatMovieDetails_noGenres_shouldReturnFormattedStringWithUnknownGenre() {
        //Arrange
        val movie = Movie(
            id = 1,
            title = "Unknown Genre Movie",
            poster = null,
            releaseDate = "2025-12-10",
            voteAverage = 8.2,
            genres = emptyList()
        )

        //Act
        val formattedDetails = movie.formatMovieDetails()

        //Assert
        assertEquals("2025 · ??? · 8.2/10", formattedDetails)
    }

    @Test
    fun formatMovieDetails_emptyReleaseDate_shouldReturnFormattedStringWithUnknownReleaseDate() {
        //Arrange
        val movie = Movie(
            id = 1,
            title = "No Date Movie",
            poster = null,
            releaseDate = "",
            voteAverage = 8.2,
            genres = listOf(Genre(1, "Action"))
        )

        //Act
        val formattedDetails = movie.formatMovieDetails()

        //Assert
        assertEquals("??? · Action · 8.2/10", formattedDetails)
    }

    @Test
    fun formatMovieDetails_emptyVoteAverage_shouldReturnFormattedStringWithUnknownVoteAverage() {
        //Arrange
        val movie = Movie(
            id = 1,
            title = "No Date Movie",
            poster = null,
            releaseDate = "2025-12-10",
            voteAverage = 0.0,
            genres = listOf(Genre(1, "Action"))
        )

        //Act
        val formattedDetails = movie.formatMovieDetails()

        //Assert
        assertEquals("2025 · Action · ???", formattedDetails)
    }

    @Test
    fun formatMovieDetails_voteAverageWithMoreThanOneDecimalPlace_shouldReturnFormattedStringWithOneDecimalPlaces() {
        //Arrange
        val movie = Movie(
            id = 1,
            title = "No Date Movie",
            poster = null,
            releaseDate = "2025-12-10",
            voteAverage = 8.2321,
            genres = listOf(Genre(1, "Action"))
        )

        //Act
        val formattedDetails = movie.formatMovieDetails()

        //Assert
        assertEquals("2025 · Action · 8.2/10", formattedDetails)
    }
}