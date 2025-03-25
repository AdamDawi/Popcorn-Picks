package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import app.cash.turbine.test
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    private lateinit var genresPreferences: GenresPreferences
    private lateinit var likedMoviesDbRepository: LikedMoviesDbRepository
    private lateinit var sut: ProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        genresPreferences = mockk()
        likedMoviesDbRepository = mockk()
        sut = ProfileViewModel(
            genresPreferences,
            likedMoviesDbRepository,
            replaceMainDispatcherRule.testDispatcher
        )
    }

    // GET GENRES
    @Test
    fun getGenres_genresExistInPreferences_genresStateUpdatedWithCorrectGenres() = runTest{
        // Arrange
        val genresIds = listOf(
            "14",
            "36"
        )
        val genres = listOf(
            Genre(id = 14, name = "Fantasy"),
            Genre(id = 36, name = "History")
        )
        every { genresPreferences.getGenres() } answers { genresIds }
        coEvery { likedMoviesDbRepository.getLikedMoviesCount() } answers { Result.Success(0)}

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertTrue(updatedState.genres.containsAll(genres))
        }
    }

    @Test
    fun getGenres_genresNotExistInPreferences_genresStateUpdatedWithEmptyList() = runTest{
        // Arrange
        every { genresPreferences.getGenres() } answers { emptyList() }
        coEvery { likedMoviesDbRepository.getLikedMoviesCount() } answers { Result.Success(0)}

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertTrue(updatedState.genres.isEmpty())
        }
    }
}