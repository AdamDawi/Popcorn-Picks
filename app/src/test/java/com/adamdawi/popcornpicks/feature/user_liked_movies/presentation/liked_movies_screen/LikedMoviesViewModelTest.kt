package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen

import app.cash.turbine.test
import com.adamdawi.popcornpicks.core.data.dummy.dummyLikedMovie
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LikedMoviesViewModelTest {

    private lateinit var likedMoviesDbRepository: LikedMoviesDbRepository
    private lateinit var sut: LikedMoviesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        likedMoviesDbRepository = mockk()
        sut = LikedMoviesViewModel(
            likedMoviesDbRepository = likedMoviesDbRepository,
            ioDispatcher = replaceMainDispatcherRule.testDispatcher
        )
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(emptyList())
        }
    }

    // INITIAL
    @Test
    fun init_movies_moviesListStateEqualsToEmptyList() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } coAnswers {
            delay(500)
            Result.Success(emptyList())
        }

        sut.state.test{
            // Act
            val state = awaitItem()

            // Assert
            assertTrue(state.movies.isEmpty())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun init_error_errorStateEqualsToNull() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } coAnswers {
            delay(500)
            Result.Success(emptyList())
        }

        sut.state.test{
            // Act
            val state = awaitItem()

            // Assert
            assertThat(state.error).isEqualTo(null)
            ensureAllEventsConsumed()
        }
    }
    // GET LIKED MOVIES
    @Test
    fun getLikedMovies_success_moviesStateUpdatedWithCorrectMovies() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOf(dummyLikedMovie))
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.movies).isEqualTo(listOf(dummyLikedMovie))
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getLikedMovies_error_errorStateUpdatedWithCorrectError() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers { Result.Error(DataError.Local.UNKNOWN)}

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.error).isEqualTo(DataError.Local.UNKNOWN.asUiText())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getLikedMovies_error_moviesStateEqualsToEmptyList() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers { Result.Error(DataError.Local.UNKNOWN)}

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.movies).isEqualTo(emptyList<LikedMovie>())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getLikedMovies_loadingStateIsSetToTrueWhileFetching() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } coAnswers {
            delay(500)
            Result.Success(emptyList())
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
    fun getLikedMovies_success_loadingStateIsSetToFalseAfterFetching() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers { Result.Success(emptyList()) }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isEqualTo(false)
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getLikedMovies_error_loadingStateIsSetToFalseAfterFetching() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers { Result.Error(DataError.Local.UNKNOWN) }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isEqualTo(false)
            ensureAllEventsConsumed()
        }
    }

}