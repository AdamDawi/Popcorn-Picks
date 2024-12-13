package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre
import com.adamdawi.popcornpicks.feature.genres_choose.domain.repository.GenresRepository
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class GenresViewModelTest {

    private lateinit var sut: GenresViewModel
    private lateinit var genresRepository: GenresRepository

    @Before
    fun setUp(){
        genresRepository = mockk<GenresRepository>()
        every { genresRepository.getGenres() } answers { dummyGenresList }
        sut = GenresViewModel(genresRepository)
    }

    //INIT
    @Test
    fun getGenres_genres_updateGenres(){
        //Arrange

        //Act

        //Assert
        assertThat(sut.state.value.genres, `is`(dummyGenresList))
    }

    @Test
    fun init_selectedGenres_empty(){
        //Arrange

        //Act

        //Assert
        assertThat(sut.state.value.selectedGenres.size, `is`(0))
    }

    //ON GENRE CLICK
    @Test
    fun onAction_toggleGenreSelectionOnce_genreSelected(){
        //Arrange
        val genre = Genre(id = 36, name = "History")

        //Act
        sut.onAction(GenresAction.ToggleGenreSelection(genre))

        //Assert
        assertThat(sut.state.value.selectedGenres.size, `is`(1))
        assertThat(sut.state.value.selectedGenres.contains(genre), `is`(true))
    }

    @Test
    fun onAction_toggleGenreSelectionOnSameGenreTwice_genreDeselected(){
        //Arrange
        val genre = Genre(id = 36, name = "History")

        //Act
        sut.onAction(GenresAction.ToggleGenreSelection(genre))
        sut.onAction(GenresAction.ToggleGenreSelection(genre))

        //Assert
        assertThat(sut.state.value.selectedGenres.size, `is`(0))
        assertThat(sut.state.value.selectedGenres.contains(genre), `is`(false))
    }

    @Test
    fun onAction_toggleGenreSelectionOnDifferentGenresTwice_continueButtonEnabled(){
        //Arrange
        val genre = Genre(id = 36, name = "History")
        val genre2 = Genre(id = 80, name = "Crime")

        //Act
        sut.onAction(GenresAction.ToggleGenreSelection(genre))
        sut.onAction(GenresAction.ToggleGenreSelection(genre2))

        //Assert
        assertThat(sut.state.value.continueButtonEnabled, `is`(true))
    }

    @Test
    fun onAction_toggleGenreSelectionOnSameGenreTwice_continueButtonDisabled(){
        //Arrange
        val genre = Genre(id = 36, name = "History")

        //Act
        sut.onAction(GenresAction.ToggleGenreSelection(genre))
        sut.onAction(GenresAction.ToggleGenreSelection(genre))

        //Assert
        assertThat(sut.state.value.continueButtonEnabled, `is`(false))
    }
}