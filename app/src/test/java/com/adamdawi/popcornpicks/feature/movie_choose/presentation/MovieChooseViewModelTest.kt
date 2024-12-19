package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.OnBoardingManager
import com.adamdawi.popcornpicks.core.domain.repository.MoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import com.adamdawi.popcornpicks.feature.movie_choose.domain.repository.MovieChooseRepository
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import com.google.common.truth.Truth.assertThat
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
    private lateinit var onBoardingManager: OnBoardingManager
    private lateinit var moviesDbRepository: MoviesDbRepository
    private lateinit var sut: MovieChooseViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setUp() {
        movieChooseRepository = mockk<MovieChooseRepository>()
        genresPreferences = mockk<GenresPreferences>()
        onBoardingManager = mockk<OnBoardingManager>()
        moviesDbRepository = mockk<MoviesDbRepository>()
    }

    //TODO make tests for saving movies to db
    //GET GENRES IDS
    @Test
    fun getGenresIds_genresNotSavedInSharedPreferences_defaultGenresIdsListPassedToGetMovies() {
        // Arrange
        val defaultGenresIDs = Constants.Local.DEFAULT_GENRES_IDS
        every { genresPreferences.getGenres() } returns emptyList()

        // Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        // Assert
        coVerify(exactly = 1) { movieChooseRepository.getMovies(defaultGenresIDs[0], 1) }
        coVerify(exactly = 1) { movieChooseRepository.getMovies(defaultGenresIDs[1], 1) }
        coVerify(exactly = 1) { movieChooseRepository.getMovies(defaultGenresIDs[2], 1) }
    }

    @Test
    fun getGenresIds_genresSavedInSharedPreferences_correctGenresIdsListPassedToGetMovies() {
        // Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs

        // Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        // Assert
        coVerify(exactly = 1) { movieChooseRepository.getMovies(genresIDs[0], 1) }
        coVerify(exactly = 1) { movieChooseRepository.getMovies(genresIDs[1], 1) }
        coVerify(exactly = 1) { movieChooseRepository.getMovies(genresIDs[2], 1) }
    }

    //GET MOVIES
    @Test
    fun getMovies_success_movieListStateUpdatedWithCorrectData() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        val movieListID1 = listOf(
            Movie(
                id = 1,
                title = "Spiderman",
                poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                releaseDate = "2020-04-02"
            )
        )

        val movieListID2 = listOf(
            Movie(
                id = 2,
                title = "Thor",
                poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                releaseDate = "2022-11-06"
            )
        )
        val movieListID3 = listOf(
            Movie(
                id = 33,
                title = "Iron Man",
                poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                releaseDate = "2023-12-01"
            )
        )
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(genresIDs[0], any()) } answers {
            Result.Success(
                movieListID1
            )
        }

        coEvery { movieChooseRepository.getMovies(genresIDs[1], any()) } answers {
            Result.Success(
                movieListID2
            )
        }

        coEvery { movieChooseRepository.getMovies(genresIDs[2], any()) } answers {
            Result.Success(
                movieListID3
            )
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        val expectedMovies = movieListID1 + movieListID1 + movieListID1 +
                movieListID2 + movieListID2 + movieListID2 +
                movieListID3 + movieListID3 + movieListID3
        assertThat(sut.state.value.movies).containsExactlyElementsIn(expectedMovies)
    }

    @Test
    fun getMovies_success_repositoryGetMoviesInvokedCorrectNumberOfTimes() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(any(), any()) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        coVerify(exactly = 1){ movieChooseRepository.getMovies(genresIDs[0], 1) }
        coVerify(exactly = 1){ movieChooseRepository.getMovies(genresIDs[0], 2) }
        coVerify(exactly = 1){ movieChooseRepository.getMovies(genresIDs[0], 3) }

        coVerify(exactly = 1){ movieChooseRepository.getMovies(genresIDs[1], 1) }
        coVerify(exactly = 1){ movieChooseRepository.getMovies(genresIDs[1], 2) }
        coVerify(exactly = 1){ movieChooseRepository.getMovies(genresIDs[1], 3) }

        coVerify(exactly = 1){ movieChooseRepository.getMovies(genresIDs[2], 1) }
        coVerify(exactly = 1){ movieChooseRepository.getMovies(genresIDs[2], 2) }
        coVerify(exactly = 1){ movieChooseRepository.getMovies(genresIDs[2], 3) }
    }

    @Test
    fun getMovies_success_errorStateIsNotUpdated() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(any(), any()) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        assertThat(sut.state.value.error.isNullOrEmpty(), `is`(true))
    }

    @Test
    fun getMovies_success_loadingStateIsSetToTrueWhileFetching() {
        // Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(any(), any()) } coAnswers {
            delay(1000)
            Result.Success(
                dummyMovieList
            )
        }

        // Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(true))
    }

    @Test
    fun getMovies_success_loadingStateIsSetToFalseAfterFetching() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(any(), any()) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(false))
    }

    @Test
    fun getMovies_error_errorStateUpdatedWithCorrectError() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(any(), any()) } answers {
            Result<List<Genre>, DataError.Network>.Error(
                DataError.Network.SERVER_ERROR
            )
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        assertThat(sut.state.value.error, `is`(DataError.Network.SERVER_ERROR.asUiText()))
    }

    @Test
    fun getMovies_error_moviesListIsNotUpdated() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(any(), any()) } answers {
            Result<List<Genre>, DataError.Network>.Error(
                DataError.Network.SERVER_ERROR
            )
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        assertThat(sut.state.value.movies, `is`(emptyList()))
    }

    @Test
    fun getMovies_error_loadingStateIsSetToTrueWhileFetching() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(any(), any()) } coAnswers {
            delay(1000)
            Result<List<Genre>, DataError.Network>.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(true))
    }

    @Test
    fun getMovies_error_loadingStateIsSetToFalseAfterFetching() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(any(), any()) } answers {
            Result<List<Genre>, DataError.Network>.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(false))
    }

    @Test
    fun getMovies_successThenError_errorStateUpdatedWithCorrectError() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { movieChooseRepository.getMovies(any(), 1) } answers {
            Result<List<Genre>, DataError.Network>.Success(dummyMovieList)
        }
        coEvery { movieChooseRepository.getMovies(any(), 2) } answers {
            Result<List<Genre>, DataError.Network>.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        assertThat(sut.state.value.error, `is`(DataError.Network.SERVER_ERROR.asUiText()))
    }

    //INIT
    @Test
    fun init_selectedGenres_empty() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            movieChooseRepository.getMovies(
                any(),
                any()
            )
        } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(0))
    }

    //TOGGLE MOVIE SELECTION
    @Test
    fun onAction_toggleMovieSelectionOnce_movieSelected() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            movieChooseRepository.getMovies(
                any(),
                any()
            )
        } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02"
        )

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(1))
        assertThat(sut.state.value.selectedMovies.contains(movie), `is`(true))
    }

    @Test
    fun onAction_toggleMovieSelectionOnSameMovieTwice_movieDeselected() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            movieChooseRepository.getMovies(
                any(),
                any()
            )
        } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02"
        )

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(0))
        assertThat(sut.state.value.selectedMovies.contains(movie), `is`(false))
    }

    @Test
    fun onAction_toggleMovieSelectionOnSameMovieThreeTimes_finishButtonDisabled() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            movieChooseRepository.getMovies(
                any(),
                any()
            )
        } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02"
        )

        //Act
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))

        //Assert
        assertThat(sut.state.value.finishButtonEnabled, `is`(false))
    }

    @Test
    fun onAction_toggleMovieSelectionOnThreeDifferentMovies_threeDifferentMoviesSelected() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            movieChooseRepository.getMovies(
                any(),
                any()
            )
        } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
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
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)
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
    fun onAction_toggleMovieSelectionOnThreeDifferentMovies_finishButtonEnabled() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            movieChooseRepository.getMovies(
                any(),
                any()
            )
        } answers { Result<List<Genre>, DataError.Network>.Success(dummyMovieList) }
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
        sut = MovieChooseViewModel(movieChooseRepository, genresPreferences, onBoardingManager, moviesDbRepository)
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))

        //Assert
        assertThat(sut.state.value.finishButtonEnabled, `is`(true))
    }
}