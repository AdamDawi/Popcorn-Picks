package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen

import app.cash.turbine.test
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.OnBoardingManager
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.onboarding.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
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

    private lateinit var remoteMovieRecommendationsRepository: RemoteMovieRecommendationsRepository
    private lateinit var genresPreferences: GenresPreferences
    private lateinit var onBoardingManager: OnBoardingManager
    private lateinit var likedMoviesDbRepository: LikedMoviesDbRepository
    private lateinit var sut: MovieChooseViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        remoteMovieRecommendationsRepository = mockk<RemoteMovieRecommendationsRepository>()
        genresPreferences = mockk<GenresPreferences>()
        onBoardingManager = mockk<OnBoardingManager>()
        likedMoviesDbRepository = mockk<LikedMoviesDbRepository>()
        sut = MovieChooseViewModel(
            remoteMovieRecommendationsRepository,
            genresPreferences,
            onBoardingManager,
            likedMoviesDbRepository,
            replaceMainDispatcherRule.testDispatcher
        )
    }

    //GET GENRES IDS
    @Test
    fun getGenresIds_genresNotSavedInSharedPreferences_defaultGenresIdsListPassedToGetMovies() = runTest {
        // Arrange
        val defaultGenresIDs = Constants.Local.DEFAULT_GENRES_IDS
        every { genresPreferences.getGenres() } returns emptyList()
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(emptyList())
        }

        // Act
        sut.state.test{
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(defaultGenresIDs[0], 1) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(defaultGenresIDs[1], 1) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(defaultGenresIDs[2], 1) }
    }

    @Test
    fun getGenresIds_genresSavedInSharedPreferences_correctGenresIdsListPassedToGetMovies() = runTest {
        // Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(emptyList())
        }

        // Act
        sut.state.test{
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[0], 1) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[1], 1) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[2], 1) }
    }

    //GET MOVIES
    @Test
    fun getMovies_success_movieListStateUpdatedWithCorrectData() = runTest {
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
        val expectedMovies = movieListID1 + movieListID1 + movieListID1 +
                movieListID2 + movieListID2 + movieListID2 +
                movieListID3 + movieListID3 + movieListID3
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[0], any()) } answers {
            Result.Success(
                movieListID1
            )
        }

        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[1], any()) } answers {
            Result.Success(
                movieListID2
            )
        }

        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[2], any()) } answers {
            Result.Success(
                movieListID3
            )
        }

        sut.state.test{
            //Act
            val movies = awaitItem().movies

            //Assert
            assertThat(movies).containsExactlyElementsIn(expectedMovies)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getMovies_success_repositoryGetMoviesInvokedCorrectNumberOfTimes() = runTest {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        //Act
        sut.state.test{
            cancelAndIgnoreRemainingEvents()
        }

        //Assert
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[0], 1) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[0], 2) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[0], 3) }

        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[1], 1) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[1], 2) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[1], 3) }

        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[2], 1) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[2], 2) }
        coVerify(exactly = 1) { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genresIDs[2], 3) }
    }

    @Test
    fun getMovies_success_errorStateIsNotUpdated() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        sut.state.test{
            //Act
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.error.isNullOrEmpty(), `is`(true))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovies_success_loadingStateIsSetToTrueWhileFetching() = runTest{
        // Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } coAnswers {
            delay(500)
            Result.Success(
                dummyMovieList
            )
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.isLoading, `is`(true))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovies_success_loadingStateIsSetToFalseAfterFetching() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(
                dummyMovieList
            )
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.isLoading, `is`(false))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovies_error_errorStateUpdatedWithCorrectError() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Error(
                DataError.Network.SERVER_ERROR
            )
        }

        sut.state.test{
            //Act
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.error, `is`(DataError.Network.SERVER_ERROR.asUiText()))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovies_error_moviesListIsNotUpdated() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Error(
                DataError.Network.SERVER_ERROR
            )
        }

        sut.state.test{
            //Act
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.movies, `is`(emptyList()))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovies_error_loadingStateIsSetToTrueWhileFetching() = runTest {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } coAnswers {
            delay(500)
            Result.Error(DataError.Network.SERVER_ERROR)
        }


        sut.state.test{
            //Act
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.isLoading, `is`(true))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovies_error_loadingStateIsSetToFalseAfterFetching() = runTest {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Error(DataError.Network.SERVER_ERROR)
        }

        sut.state.test{
            //Act
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.isLoading, `is`(false))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun getMovies_successThenError_errorStateUpdatedWithCorrectError() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), 1) } answers {
            Result.Success(dummyMovieList)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), 2) } answers {
            Result.Error(DataError.Network.SERVER_ERROR)
        }


        sut.state.test{
            //Act
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.error, `is`(DataError.Network.SERVER_ERROR.asUiText()))

            ensureAllEventsConsumed()
        }
    }

    //INIT
    @Test
    fun init_selectedGenres_empty() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }

        sut.state.test{
            //Act
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.selectedMovies.size, `is`(0))

            ensureAllEventsConsumed()
        }
    }

    //TOGGLE MOVIE SELECTION
    @Test
    fun toggleMovieSelection_toggleMovieSelectionOnce_movieSelected() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
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

        sut.state.test{
            skipItems(1) //Initial state

            //Act
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.selectedMovies.size, `is`(1))
            assertThat(updatedState.selectedMovies.contains(movie), `is`(true))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun toggleMovieSelection_toggleMovieSelectionOnSameMovieTwice_movieDeselected() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
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

        sut.state.test{
            skipItems(1) //Initial state

            //Act
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            skipItems(1)
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.selectedMovies.size, `is`(0))
            assertThat(updatedState.selectedMovies.contains(movie), `is`(false))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun toggleMovieSelection_toggleMovieSelectionOnSameMovieThreeTimes_finishButtonDisabled() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
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

        sut.state.test{
            skipItems(1) //Initial state

            //Act
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            skipItems(1)
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            skipItems(1)
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.finishButtonEnabled, `is`(false))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun toggleMovieSelection_toggleMovieSelectionOnThreeDifferentMovies_threeDifferentMoviesSelected() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
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


        sut.state.test{
            skipItems(1) //Initial state

            //Act
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            skipItems(1)
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
            skipItems(1)
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.selectedMovies.size, `is`(3))
            assertThat(updatedState.selectedMovies.contains(movie), `is`(true))
            assertThat(updatedState.selectedMovies.contains(movie2), `is`(true))
            assertThat(updatedState.selectedMovies.contains(movie3), `is`(true))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun toggleMovieSelection_toggleMovieSelectionOnThreeDifferentMovies_finishButtonEnabled() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
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

        sut.state.test{
            skipItems(1) //Initial state

            //Act
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            skipItems(1)
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
            skipItems(1)
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))
            val updatedState = awaitItem()

            //Assert
            assertThat(updatedState.finishButtonEnabled, `is`(true))

            ensureAllEventsConsumed()
        }
    }

    //ADD MOVIES TO DB
    @Test
    fun addMoviesToDb_success_addMoviesInvokedOnceWithCorrectSelectedMovies() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { likedMoviesDbRepository.addLikedMovies(any()) } answers {
            Result.Success(Unit)
        }
        every { onBoardingManager.setOnboardingCompleted(any()) } answers {}

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

        sut.state.test{
            skipItems(1) //Initial state

            //Act
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))
            sut.onAction(MovieChooseAction.OnFinishClick)

            skipItems(3)

            ensureAllEventsConsumed()
        }

        //Assert
        coVerify(exactly = 1) { likedMoviesDbRepository.addLikedMovies(listOf(movie, movie2, movie3)) }
    }

    @Test
    fun addMoviesToDb_success_setOnboardingCompletedInvokedOnceWithTrue() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { likedMoviesDbRepository.addLikedMovies(any()) } answers {
            Result.Success(Unit)
        }
        every { onBoardingManager.setOnboardingCompleted(any()) } answers {}

        sut.state.test{
            skipItems(1) //Initial state

            //Act
            sut.onAction(MovieChooseAction.OnFinishClick)

            ensureAllEventsConsumed()
        }

        //Assert
        verify(exactly = 1) { onBoardingManager.setOnboardingCompleted(true) }
    }

    @Test
    fun addMoviesToDb_success_eventChannelUpdatedWithSuccess() = runTest {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        every { onBoardingManager.setOnboardingCompleted(true) } just Runs
        coEvery { likedMoviesDbRepository.addLikedMovies(any()) } answers {
            Result.Success(Unit)
        }

        sut.events.test {
            //Act
            sut.onAction(MovieChooseAction.OnFinishClick)
            val emission = awaitItem()
            //Assert
            assertThat(emission, `is`(MovieChooseEvent.Success))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun addMoviesToDb_error_addMoviesInvokedOnceWithCorrectSelectedMovies() = runTest {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { likedMoviesDbRepository.addLikedMovies(any()) } answers {
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

        sut.state.test{
            skipItems(1) //Initial state

            //Act
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie))
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie2))
            sut.onAction(MovieChooseAction.ToggleMovieSelection(movie3))
            sut.onAction(MovieChooseAction.OnFinishClick)

            skipItems(3)
            ensureAllEventsConsumed()
        }

        //Assert
        coVerify(exactly = 1) { likedMoviesDbRepository.addLikedMovies(listOf(movie, movie2, movie3)) }
    }

    @Test
    fun addMoviesToDb_error_setOnboardingCompletedNotInvoked() = runTest{
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { likedMoviesDbRepository.addLikedMovies(any()) } answers {
            Result.Error(DataError.Local.DISK_FULL)
        }

        sut.state.test{
            skipItems(1) //Initial state

            //Act
            sut.onAction(MovieChooseAction.OnFinishClick)

            ensureAllEventsConsumed()
        }

        //Assert
        verify(exactly = 0) { onBoardingManager.setOnboardingCompleted(any()) }
    }

    @Test
    fun addMoviesToDb_error_eventChannelUpdatedWithCorrectError() = runTest {
        //Arrange
        val genresIDs = listOf("3", "12", "878")
        every { genresPreferences.getGenres() } returns genresIDs
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers { Result.Success(dummyMovieList) }
        coEvery { likedMoviesDbRepository.addLikedMovies(any()) } answers {
            Result.Error(DataError.Local.DISK_FULL)
        }

        sut.events.test {
            //Act
            sut.onAction(MovieChooseAction.OnFinishClick)
            val emission = awaitItem()

            //Assert
            assertTrue(emission is MovieChooseEvent.Error && emission.error == DataError.Local.DISK_FULL.asUiText())

            ensureAllEventsConsumed()
        }
    }
}