package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import app.cash.turbine.test
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
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
    private lateinit var sut: ProfileViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        genresPreferences = mockk()
        sut = ProfileViewModel(
            genresPreferences
        )
    }

    // GET GENRES
    @Test
    fun getGenres_genresStateUpdatedWithCorrectGenres() = runTest{
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

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertTrue(updatedState.genres.containsAll(genres))
        }

    }
}