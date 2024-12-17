package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.domain.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import com.adamdawi.popcornpicks.feature.movie_choose.domain.repository.MovieChooseRepository
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieChooseViewModelTest {

    private lateinit var movieChooseRepository: MovieChooseRepository
    private lateinit var genresPreferences: GenresPreferences
    private lateinit var sut: MovieChooseViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setUp() {
        movieChooseRepository = mockk<MovieChooseRepository>()
        genresPreferences = mockk<GenresPreferences>()
    }

    //GET GENRES IDS
    @Test
    fun getGenresIds_genresNotSavedInSharedPreferences_defaultGenresIdsListPassedToGetMovies() {
        // Arrange
        val defaultGenresIDs = Constants.Local.DEFAULT_GENRES_IDS
        every { genresPreferences.getGenres() } returns emptyList()

        // Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        // Assert
        coVerify(exactly = 1) { movieChooseRepository.getMovies(defaultGenresIDs) }
    }

    @Test
    fun getGenresIds_genresSavedInSharedPreferences_correctGenresIdsListPassedToGetMovies() {
        // Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs

        // Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        // Assert
        coVerify(exactly = 1) { movieChooseRepository.getMovies(genresIDs) }
    }

    //GET MOVIES
    @Test
    fun getMovies_success_movieListStateUpdatedWithCorrectData() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        //Assert
        assertThat(sut.state.value.movies, `is`(dummyMovieList))
    }

    @Test
    fun getMovies_success_errorStateIsNotUpdated() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        //Assert
        assertThat(sut.state.value.error.isNullOrEmpty(), `is`(true))
    }

    @Test
    fun getMovies_success_loadingStateIsSetToTrueWhileFetching(){
        // Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } coAnswers {
            delay(1000)
            Result.Success(
                dummyMovieList
            )
        }

        // Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(true))
    }

    @Test
    fun getMovies_success_loadingStateIsSetToFalseAfterFetching(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(false))
    }

    @Test
    fun getMovies_error_errorStateUpdatedWithCorrectError(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers { Result<List<Genre>, DataError.Network>.Error(
            DataError.Network.SERVER_ERROR) }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        //Assert
        assertThat(sut.state.value.error, `is`(DataError.Network.SERVER_ERROR.asUiText()))
    }

    @Test
    fun getMovies_error_moviesListIsNotUpdated(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers { Result<List<Genre>, DataError.Network>.Error(
            DataError.Network.SERVER_ERROR) }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        //Assert
        assertThat(sut.state.value.movies, `is`(emptyList()))
    }

    @Test
    fun getMovies_error_loadingStateIsSetToTrueWhileFetching(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } coAnswers {
            delay(1000)
            Result<List<Genre>, DataError.Network>.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(true))
    }

    @Test
    fun getGenres_error_loadingStateIsSetToFalseAfterFetching(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers {
            Result<List<Genre>, DataError.Network>.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(false))
    }

    //INIT
    @Test
    fun init_selectedGenres_empty(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(0))
    }

    //TOGGLE MOVIE SELECTION
    @Test
    fun onAction_toggleMovieSelectionOnce_movieSelected(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02"
        )

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(1))
        assertThat(sut.state.value.selectedMovies.contains(movie), `is`(true))
    }

    @Test
    fun onAction_toggleMovieSelectionOnSameMovieTwice_movieDeselected(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02"
        )

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(0))
        assertThat(sut.state.value.selectedMovies.contains(movie), `is`(false))
    }

    @Test
    fun onAction_toggleMovieSelectionOnSameMovieThreeTimes_finishButtonDisabled(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02"
        )

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))

        //Assert
        assertThat(sut.state.value.finishButtonEnabled, `is`(false))
    }

    @Test
    fun onAction_toggleMovieSelectionOnThreeDifferentMovies_threeDifferentMoviesSelected(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02"
        )
        val movie2 = Movie(
            id = 2,
            title = "Thor",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2022-11-06"
        )
        val movie3 = Movie(
            id = 33,
            title = "Iron Man",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2023-12-01"
        )

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(3))
        assertThat(sut.state.value.selectedMovies.contains(movie), `is`(true))
        assertThat(sut.state.value.selectedMovies.contains(movie2), `is`(true))
        assertThat(sut.state.value.selectedMovies.contains(movie3), `is`(true))
    }

    @Test
    fun onAction_toggleMovieSelectionOnThreeDifferentMovies_finishButtonEnabled(){
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs) } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02"
        )
        val movie2 = Movie(
            id = 2,
            title = "Thor",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2022-11-06"
        )
        val movie3 = Movie(
            id = 33,
            title = "Iron Man",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2023-12-01"
        )

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences)
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))

        //Assert
        assertThat(sut.state.value.finishButtonEnabled, `is`(true))
    }
}