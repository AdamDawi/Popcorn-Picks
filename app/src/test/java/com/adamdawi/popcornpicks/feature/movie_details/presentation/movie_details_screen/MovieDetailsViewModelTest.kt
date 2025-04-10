@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.adamdawi.popcornpicks.core.data.dummy.dummyDetailedMovie
import com.adamdawi.popcornpicks.core.domain.util.Constants.NavArguments.MOVIE_ID
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.movie_details.domain.DetailedMovie
import com.adamdawi.popcornpicks.feature.movie_details.domain.remote.MovieDetailsRepository
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieDetailsViewModelTest {

    private lateinit var sut: MovieDetailsViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository

    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setUp() {
        movieDetailsRepository = mockk()
        sut = MovieDetailsViewModel(
            savedStateHandle = SavedStateHandle(mapOf(MOVIE_ID to "1")),
            movieDetailsRepository = movieDetailsRepository,
            ioDispatcher = replaceMainDispatcherRule.testDispatcher
        )
    }

    // INITIAL
    @Test
    fun init_movie_movieStateEqualsToDefaultDetailedMovie() = runTest{
        // Arrange
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } coAnswers {
            delay(500)
            Result.Error(DataError.Network.UNKNOWN)
        }

        sut.state.test{
            // Act
            val state = awaitItem()

            // Assert
            assertThat(state.movie).isEqualTo(DetailedMovie(
                id = 0,
                title = "",
                poster = null,
                genres = emptyList(),
                overview = "",
                backdrop = null,
                releaseDate = "",
                voteAverage = 0.0,
                runtime = 0,
                popularity = 0.0
            ))
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun init_error_errorStateEqualsToNull() = runTest{
        // Arrange
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } coAnswers {
            delay(500)
            Result.Success(dummyDetailedMovie)
        }

        sut.state.test{
            // Act
            val state = awaitItem()

            // Assert
            assertThat(state.error).isEqualTo(null)
            ensureAllEventsConsumed()
        }
    }

    // GET MOVIE DETAILS
    @Test
    fun getMovieDetails_success_movieStateUpdatedWithCorrectMovie() = runTest{
        // Arrange
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } answers {
            Result.Success(dummyDetailedMovie)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.movie).isEqualTo(dummyDetailedMovie)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovieDetails_error_errorStateUpdatedWithCorrectError() = runTest{
        // Arrange
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } answers {
            Result.Error(DataError.Network.UNKNOWN)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.error).isEqualTo(DataError.Network.UNKNOWN.asUiText())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovieDetails_error_movieStateEqualsToDefaultDetailedMovie() = runTest{
        // Arrange
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } answers { Result.Error(DataError.Network.UNKNOWN)}

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.movie).isEqualTo(DetailedMovie(
                id = 0,
                title = "",
                poster = null,
                genres = emptyList(),
                overview = "",
                backdrop = null,
                releaseDate = "",
                voteAverage = 0.0,
                runtime = 0,
                popularity = 0.0
            ))
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovieDetails_loadingStateIsSetToTrueWhileFetching() = runTest{
        // Arrange
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } coAnswers {
            delay(500)
            Result.Success(dummyDetailedMovie)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isEqualTo(true)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovieDetails_success_loadingStateIsSetToFalseAfterFetching() = runTest{
        // Arrange
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } answers { Result.Success(dummyDetailedMovie) }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isEqualTo(false)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovieDetails_error_loadingStateIsSetToFalseAfterFetching() = runTest{
        // Arrange
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } answers { Result.Error(DataError.Network.UNKNOWN) }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isEqualTo(false)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovieDetails_movieIdIsNull_errorStateUpdatedWithCorrectError() = runTest{
        // Arrange
        sut = MovieDetailsViewModel(
            savedStateHandle = SavedStateHandle(),
            movieDetailsRepository = movieDetailsRepository,
            ioDispatcher = replaceMainDispatcherRule.testDispatcher
        )
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } answers { Result.Error(DataError.Network.UNKNOWN) }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.error).isEqualTo("Something went wrong while loading this movie. Please try again.")
            ensureAllEventsConsumed()
        }
    }

}