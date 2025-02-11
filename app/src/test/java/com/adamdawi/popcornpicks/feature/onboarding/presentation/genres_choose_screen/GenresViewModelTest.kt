package com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen

import app.cash.turbine.test
import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.GenresRepository
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GenresViewModelTest {

    private lateinit var sut: GenresViewModel
    private lateinit var genresRepository: GenresRepository
    private lateinit var genresPreferences: GenresPreferences

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setUp(){
        genresPreferences = mockk<GenresPreferences>(relaxed = true)
        genresRepository = mockk<GenresRepository>()
        sut = GenresViewModel(genresRepository, genresPreferences)
    }

    //GET GENRES
    @Test
    fun getGenres_success_genresListStateUpdatedWithCorrectData() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } answers {
            Result.Success(dummyGenresList)
        }

        //Act
        val state = sut.state.first()

        //Assert
        assertThat(state.genres, `is`(dummyGenresList))
    }

    @Test
    fun getGenres_success_errorStateIsNotUpdated() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } coAnswers {
            delay(500)
            Result.Success(dummyGenresList)
        }

        //Act
        val states = sut.state.take(2).toList()

        //Assert
        assertThat(states.last().error.isNullOrEmpty(), `is`(true))
    }

    @Test
    fun getGenres_success_loadingStateIsSetToTrueWhileFetching() = runTest{
        // Arrange
        coEvery { genresRepository.getGenres() } coAnswers {
            delay(500)
            Result.Success(dummyGenresList)
        }

        // Act
        val state = sut.state.first()

        //Assert
        assertThat(state.isLoading, `is`(true))
    }

    @Test
    fun getGenres_success_loadingStateIsSetToFalseAfterFetching() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } coAnswers {
            delay(500)
            Result.Success(dummyGenresList)
        }

        //Act
        val states = sut.state.take(2).toList()

        // Assert
        assertThat(states.last().isLoading, `is`(false))
    }

    @Test
    fun getGenres_error_errorStateUpdatedWithCorrectError() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } answers {
            Result.Error(
            DataError.Network.SERVER_ERROR)
        }

        //Act
        val state = sut.state.first()

        //Assert
        assertThat(state.error, `is`(DataError.Network.SERVER_ERROR.asUiText()))
    }

    @Test
    fun getGenres_error_genresListIsNotUpdated() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } coAnswers {
            delay(500)
            Result.Error(
                DataError.Network.SERVER_ERROR
            )
        }

        //Act
        val states = sut.state.take(2).toList()

        //Assert
        assertThat(states.last().genres, `is`(emptyList()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getGenres_error_loadingStateIsSetToTrueWhileFetching() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } coAnswers {
            delay(500)
            Result.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        val state = sut.state.first()

        //Assert
        assertThat(state.isLoading, `is`(true))
    }

    @Test
    fun getGenres_error_loadingStateIsSetToFalseAfterFetching() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } coAnswers {
            delay(500)
            Result.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        val states = sut.state.take(2).toList()

        //Assert
        assertThat(states.last().isLoading, `is`(false))
    }

    //INIT
    @Test
    fun init_selectedGenres_empty() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } coAnswers {
            delay(500)
            Result.Success(dummyGenresList)
        }

        //Act
        val states = sut.state.take(2).toList()

        //Assert
        assertThat(states.last().selectedGenres.size, `is`(0))
    }

    //TOGGLE GENRE SELECTION
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun onAction_toggleGenreSelectionOnce_genreSelected() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } answers {
            Result.Success(dummyGenresList)
        }
        val genre = Genre(id = 36, name = "History")

        sut.state.test {
            skipItems(1) // Initial state

            //Act
            sut.onAction(GenresAction.ToggleGenreSelection(genre))

            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.selectedGenres.size, `is`(1))
            assertThat(updatedState.selectedGenres.contains(genre), `is`(true))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun onAction_toggleGenreSelectionOnSameGenreTwice_genreDeselected() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } answers {
            Result.Success(dummyGenresList)
        }
        val genre = Genre(id = 36, name = "History")

        sut.state.test {
            skipItems(1) // Initial state

            //Act
            sut.onAction(GenresAction.ToggleGenreSelection(genre))
            awaitItem()
            sut.onAction(GenresAction.ToggleGenreSelection(genre))
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.selectedGenres.size, `is`(0))
            assertThat(updatedState.selectedGenres.contains(genre), `is`(false))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun onAction_toggleGenreSelectionOnSameGenreTwice_continueButtonDisabled() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } answers {
            Result.Success(dummyGenresList)
        }
        val genre = Genre(id = 36, name = "History")

        sut.state.test {
            skipItems(1) // Initial state

            //Act
            sut.onAction(GenresAction.ToggleGenreSelection(genre))
            awaitItem()
            sut.onAction(GenresAction.ToggleGenreSelection(genre))
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.continueButtonEnabled, `is`(false))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun onAction_toggleGenreSelectionOnDifferentGenresTwice_bothGenresSelected() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } answers {
            Result.Success(dummyGenresList)
        }
        val genre = Genre(id = 36, name = "History")
        val genre2 = Genre(id = 80, name = "Crime")

        sut.state.test {
            skipItems(1) // Initial state

            //Act
            sut.onAction(GenresAction.ToggleGenreSelection(genre))
            awaitItem()
            sut.onAction(GenresAction.ToggleGenreSelection(genre2))
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.selectedGenres.size, `is`(2))
            assertThat(updatedState.selectedGenres.contains(genre), `is`(true))
            assertThat(updatedState.selectedGenres.contains(genre2), `is`(true))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun onAction_toggleGenreSelectionOnDifferentGenresTwice_continueButtonEnabled() = runTest{
        //Arrange
        coEvery { genresRepository.getGenres() } answers {
            Result.Success(dummyGenresList)
        }
        val genre = Genre(id = 36, name = "History")
        val genre2 = Genre(id = 80, name = "Crime")

        sut.state.test {
            skipItems(1) // Initial state
            //Act
            sut.onAction(GenresAction.ToggleGenreSelection(genre))
            awaitItem()
            sut.onAction(GenresAction.ToggleGenreSelection(genre2))
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.continueButtonEnabled, `is`(true))

            ensureAllEventsConsumed()
        }
    }

    // ON CONTINUE CLICK
    @Test
    fun onAction_onContinueClick_genresSavedInPreferences() = runTest{
        // Arrange
        coEvery { genresRepository.getGenres() } answers {
            Result.Success(dummyGenresList)
        }
        val selectedGenres = listOf(
            Genre(id = 36, name = "History"),
            Genre(id = 80, name = "Crime")
        )
        sut.state.test {
            skipItems(1) // Initial state

            sut.onAction(GenresAction.ToggleGenreSelection(selectedGenres[0]))
            skipItems(1)
            sut.onAction(GenresAction.ToggleGenreSelection(selectedGenres[1]))
            skipItems(1)

            //Act
            sut.onAction(GenresAction.OnContinueClick)

            ensureAllEventsConsumed()
        }

        // Assert
        verify(exactly = 1) { genresPreferences.saveGenres(selectedGenres) }
    }
}