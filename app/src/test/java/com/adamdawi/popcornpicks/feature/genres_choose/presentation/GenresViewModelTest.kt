package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre
import com.adamdawi.popcornpicks.feature.genres_choose.domain.repository.GenresRepository
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GenresViewModelTest {

    private lateinit var sut: GenresViewModel
    private lateinit var genresRepository: GenresRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setUp(){
        genresRepository = mockk<GenresRepository>()
    }

    //GET GENRES
    @Test
    fun getGenres_success_genresListUpdatedWithCorrectData(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Success(dummyGenresList) }

        //Act
        sut = GenresViewModel(genresRepository)

        //Assert
        assertThat(sut.state.value.genres, `is`(dummyGenresList))
    }

    @Test
    fun getGenres_success_errorStateIsNotUpdated(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Success(dummyGenresList) }

        //Act
        sut = GenresViewModel(genresRepository)

        //Assert
        assertThat(sut.state.value.error.isNullOrEmpty(), `is`(true))
    }

    @Test
    fun getGenres_success_loadingStateIsSetToTrueWhileFetching(){
        // Arrange
        coEvery { genresRepository.getGenres() } coAnswers {
            delay(1000)
            Result.Success(dummyGenresList)
        }

        // Act
        sut = GenresViewModel(genresRepository)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(true))
    }

    @Test
    fun getGenres_success_loadingStateIsSetToFalseAfterFetching(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Success(dummyGenresList) }

        //Act
        sut = GenresViewModel(genresRepository)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(false))
    }

    @Test
    fun getGenres_error_errorStateUpdatedWithCorrectError(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Error(
            DataError.Network.SERVER_ERROR) }

        //Act
        sut = GenresViewModel(genresRepository)

        //Assert
        assertThat(sut.state.value.error, `is`(DataError.Network.SERVER_ERROR.asUiText()))
    }

    @Test
    fun getGenres_error_genresListIsNotUpdated(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Error(
            DataError.Network.SERVER_ERROR) }

        //Act
        sut = GenresViewModel(genresRepository)

        //Assert
        assertThat(sut.state.value.genres, `is`(emptyList()))
    }

    @Test
    fun getGenres_error_loadingStateIsSetToTrueWhileFetching(){
        //Arrange
        coEvery { genresRepository.getGenres() } coAnswers {
            delay(1000)
            Result<List<Genre>, DataError.Network>.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = GenresViewModel(genresRepository)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(true))
    }

    @Test
    fun getGenres_error_loadingStateIsSetToFalseAfterFetching(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers {
            Result<List<Genre>, DataError.Network>.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = GenresViewModel(genresRepository)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(false))
    }

    //INIT
    @Test
    fun init_selectedGenres_empty(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Success(dummyGenresList) }

        //Act
        sut = GenresViewModel(genresRepository)

        //Assert
        assertThat(sut.state.value.selectedGenres.size, `is`(0))
    }

    //ON GENRE CLICK
    @Test
    fun onAction_toggleGenreSelectionOnce_genreSelected(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Success(dummyGenresList) }
        val genre = Genre(id = 36, name = "History")

        //Act
        sut = GenresViewModel(genresRepository)
        sut.onAction(GenresAction.ToggleGenreSelection(genre))

        //Assert
        assertThat(sut.state.value.selectedGenres.size, `is`(1))
        assertThat(sut.state.value.selectedGenres.contains(genre), `is`(true))
    }

    @Test
    fun onAction_toggleGenreSelectionOnSameGenreTwice_genreDeselected(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Success(dummyGenresList) }
        val genre = Genre(id = 36, name = "History")

        //Act
        sut = GenresViewModel(genresRepository)
        sut.onAction(GenresAction.ToggleGenreSelection(genre))
        sut.onAction(GenresAction.ToggleGenreSelection(genre))

        //Assert
        assertThat(sut.state.value.selectedGenres.size, `is`(0))
        assertThat(sut.state.value.selectedGenres.contains(genre), `is`(false))
    }

    @Test
    fun onAction_toggleGenreSelectionOnDifferentGenresTwice_continueButtonEnabled(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Success(dummyGenresList) }
        val genre = Genre(id = 36, name = "History")
        val genre2 = Genre(id = 80, name = "Crime")

        //Act
        sut = GenresViewModel(genresRepository)
        sut.onAction(GenresAction.ToggleGenreSelection(genre))
        sut.onAction(GenresAction.ToggleGenreSelection(genre2))

        //Assert
        assertThat(sut.state.value.continueButtonEnabled, `is`(true))
    }

    @Test
    fun onAction_toggleGenreSelectionOnSameGenreTwice_continueButtonDisabled(){
        //Arrange
        coEvery { genresRepository.getGenres() } answers { Result<List<Genre>, DataError.Network>.Success(dummyGenresList) }
        val genre = Genre(id = 36, name = "History")

        //Act
        sut = GenresViewModel(genresRepository)
        sut.onAction(GenresAction.ToggleGenreSelection(genre))
        sut.onAction(GenresAction.ToggleGenreSelection(genre))

        //Assert
        assertThat(sut.state.value.continueButtonEnabled, `is`(false))
    }
}