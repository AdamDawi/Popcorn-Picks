package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen

import app.cash.turbine.test
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.OnBoardingManager
import com.adamdawi.popcornpicks.core.domain.repository.MoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.MoviesByGenreRepository
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieChooseViewModelTest {

    private lateinit var moviesByGenreRepository: MoviesByGenreRepository
    private lateinit var genresPreferences: GenresPreferences
    private lateinit var onBoardingManager: OnBoardingManager
    private lateinit var moviesDbRepository: MoviesDbRepository
    private lateinit var sut: MovieChooseViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setUp() {
        moviesByGenreRepository = mockk<MoviesByGenreRepository>()
        genresPreferences = mockk<GenresPreferences>()
        onBoardingManager = mockk<OnBoardingManager>()
        moviesDbRepository = mockk<MoviesDbRepository>()
    }

    //GET GENRES IDS
    @Test
    fun getGenresIds_genresNotSavedInSharedPreferences_defaultGenresIdsListPassedToGetMovies() {
        // Arrange
        val defaultGenresIDs = Constants.Local.DEFAULT_GENRES_IDS
        every { genresPreferences.getGenres() } returns emptyList()

        // Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        // Assert
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(defaultGenresIDs[0], 1) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(defaultGenresIDs[1], 1) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(defaultGenresIDs[2], 1) }
    }

    @Test
    fun getGenresIds_genresSavedInSharedPreferences_correctGenresIdsListPassedToGetMovies() {
        // Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs

        // Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        // Assert
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[0], 1) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[1], 1) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[2], 1) }
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
                releaseDate = "2020-04-02",
                voteAverage = 5.0,
                genres = listOf(
                    Genre(2, "Action"),
                    Genre(6, "Comedy")
                )
            )
        )

        val movieListID2 = listOf(
            Movie(
                id = 2,
                title = "Thor",
                poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                releaseDate = "2022-11-06",
                voteAverage = 5.0,
                genres = listOf(
                    Genre(2, "Action"),
                    Genre(6, "Comedy")
                )
            )
        )
        val movieListID3 = listOf(
            Movie(
                id = 33,
                title = "Iron Man",
                poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                releaseDate = "2023-12-01",
                voteAverage = 5.0,
                genres = listOf(
                    Genre(2, "Action"),
                    Genre(6, "Comedy")
                )
            )
        )
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[0], any()) } answers {
            Result.Success(
                movieListID1
            )
        }

        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[1], any()) } answers {
            Result.Success(
                movieListID2
            )
        }

        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[2], any()) } answers {
            Result.Success(
                movieListID3
            )
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

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
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[0], 1) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[0], 2) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[0], 3) }

        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[1], 1) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[1], 2) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[1], 3) }

        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[2], 1) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[2], 2) }
        coVerify(exactly = 1) { moviesByGenreRepository.getMoviesBasedOnGenre(genresIDs[2], 3) }
    }

    @Test
    fun getMovies_success_errorStateIsNotUpdated() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        assertThat(sut.state.value.error.isNullOrEmpty(), `is`(true))
    }

    @Test
    fun getMovies_success_loadingStateIsSetToTrueWhileFetching() {
        // Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), any()) } coAnswers {
            delay(1000)
            Result.Success(
                dummyMovieList
            )
        }

        // Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        assertThat(sut.state.value.isLoading, `is`(true))
    }

    @Test
    fun getMovies_success_loadingStateIsSetToFalseAfterFetching() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        assertThat(sut.state.value.isLoading, `is`(false))
    }

    @Test
    fun getMovies_error_errorStateUpdatedWithCorrectError() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Error(
                DataError.Network.SERVER_ERROR
            )
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        assertThat(sut.state.value.error, `is`(DataError.Network.SERVER_ERROR.asUiText()))
    }

    @Test
    fun getMovies_error_moviesListIsNotUpdated() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Error(
                DataError.Network.SERVER_ERROR
            )
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        assertThat(sut.state.value.movies, `is`(emptyList()))
    }

    @Test
    fun getMovies_error_loadingStateIsSetToTrueWhileFetching() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), any()) } coAnswers {
            delay(1000)
            Result.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        assertThat(sut.state.value.isLoading, `is`(true))
    }

    @Test
    fun getMovies_error_loadingStateIsSetToFalseAfterFetching() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        assertThat(sut.state.value.isLoading, `is`(false))
    }

    @Test
    fun getMovies_successThenError_errorStateUpdatedWithCorrectError() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), 1) } answers {
            Result.Success(dummyMovieList)
        }
        coEvery { moviesByGenreRepository.getMoviesBasedOnGenre(any(), 2) } answers {
            Result.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

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
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(0))
    }

    //TOGGLE MOVIE SELECTION
    @Test
    fun toggleMovieSelection_toggleMovieSelectionOnce_movieSelected() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(1))
        assertThat(sut.state.value.selectedMovies.contains(movie), `is`(true))
    }

    @Test
    fun toggleMovieSelection_toggleMovieSelectionOnSameMovieTwice_movieDeselected() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))

        //Assert
        assertThat(sut.state.value.selectedMovies.size, `is`(0))
        assertThat(sut.state.value.selectedMovies.contains(movie), `is`(false))
    }

    @Test
    fun toggleMovieSelection_toggleMovieSelectionOnSameMovieThreeTimes_finishButtonDisabled() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))

        //Assert
        assertThat(sut.state.value.finishButtonEnabled, `is`(false))
    }

    @Test
    fun toggleMovieSelection_toggleMovieSelectionOnThreeDifferentMovies_threeDifferentMoviesSelected() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )
        val movie2 = Movie(
            id = 2,
            title = "Thor",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2022-11-06",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )
        val movie3 = Movie(
            id = 33,
            title = "Iron Man",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2023-12-01",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
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
    fun toggleMovieSelection_toggleMovieSelectionOnThreeDifferentMovies_finishButtonEnabled() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )
        val movie2 = Movie(
            id = 2,
            title = "Thor",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2022-11-06",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )
        val movie3 = Movie(
            id = 33,
            title = "Iron Man",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2023-12-01",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))

        //Assert
        assertThat(sut.state.value.finishButtonEnabled, `is`(true))
    }

    //ADD MOVIES TO DB

    @Test
    fun addMoviesToDb_success_addMoviesInvokedOnceWithCorrectSelectedMovies() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { moviesDbRepository.addMovies(any()) } answers {
            Result.Success(Unit)
        }

        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )
        val movie2 = Movie(
            id = 2,
            title = "Thor",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2022-11-06",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )
        val movie3 = Movie(
            id = 33,
            title = "Iron Man",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2023-12-01",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))
        sut.onAction(MovieChooseAction.OnFinishClick)

        //Assert
        coVerify(exactly = 1) { moviesDbRepository.addMovies(listOf(movie, movie2, movie3)) }
    }

    @Test
    fun addMoviesToDb_success_setOnboardingCompletedInvokedOnceWithTrue() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { moviesDbRepository.addMovies(any()) } answers {
            Result.Success(Unit)
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
        sut.onAction(MovieChooseAction.OnFinishClick)

        //Assert
        verify(exactly = 1) { onBoardingManager.setOnboardingCompleted(true) }
    }

    @Test
    fun addMoviesToDb_success_eventChannelUpdatedWithSuccess()= runTest {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        every { onBoardingManager.setOnboardingCompleted(true) } just Runs
        coEvery { moviesDbRepository.addMovies(any()) } answers {
            Result.Success(Unit)
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )

        //Assert
        sut.events.test {
            sut.onAction(MovieChooseAction.OnFinishClick)
            val emission = awaitItem()
            assertTrue(emission is MovieChooseEvent.Success)
            cancelAndIgnoreRemainingEvents() // Stop collecting after the first event
        }
    }

    @Test
    fun addMoviesToDb_error_addMoviesInvokedOnceWithCorrectSelectedMovies() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { moviesDbRepository.addMovies(any()) } answers {
            Result.Error(DataError.Local.DISK_FULL)
        }

        val movie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )
        val movie2 = Movie(
            id = 2,
            title = "Thor",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2022-11-06",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )
        val movie3 = Movie(
            id = 33,
            title = "Iron Man",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2023-12-01",
            voteAverage = 5.0,
            genres = listOf(
                Genre(2, "Action"),
                Genre(6, "Comedy")
            )
        )

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
        sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))
        sut.onAction(MovieChooseAction.OnFinishClick)

        //Assert
        coVerify(exactly = 1) { moviesDbRepository.addMovies(listOf(movie, movie2, movie3)) }
    }

    @Test
    fun addMoviesToDb_error_setOnboardingCompletedNotInvoked() {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { moviesDbRepository.addMovies(any()) } answers {
            Result.Error(DataError.Local.DISK_FULL)
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
        sut.onAction(MovieChooseAction.OnFinishClick)

        //Assert
        verify(exactly = 0) { onBoardingManager.setOnboardingCompleted(any()) }
    }

    @Test
    fun addMoviesToDb_error_eventChannelUpdatedWithCorrectError() = runTest {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            moviesByGenreRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { moviesDbRepository.addMovies(any()) } answers {
            Result.Error(DataError.Local.DISK_FULL)
        }

        //Act
        sut = MovieChooseViewModel(
            moviesByGenreRepository,
            genresPreferences,
            onBoardingManager,
            moviesDbRepository
        )
        //Assert
        sut.events.test {
            sut.onAction(MovieChooseAction.OnFinishClick)
            val emission = awaitItem()
            assertTrue(emission is MovieChooseEvent.Error && emission.error == DataError.Local.DISK_FULL.asUiText())
            cancelAndIgnoreRemainingEvents() // Stop collecting after the first event
        }
    }
}