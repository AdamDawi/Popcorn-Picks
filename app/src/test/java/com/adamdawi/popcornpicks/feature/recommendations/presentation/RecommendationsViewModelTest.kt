package com.adamdawi.popcornpicks.feature.recommendations.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants.SavedStateHandleArguments.IS_MOVIE_SCRATCHED
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.LocalMovieRecommendationsRepository
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.RecommendationsAction
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.RecommendationsEvent
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.RecommendationsViewModel
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecommendationsViewModelTest {

    private lateinit var sut: RecommendationsViewModel
    private lateinit var genresPreferences: GenresPreferences
    private lateinit var remoteMovieRecommendationsRepository: RemoteMovieRecommendationsRepository
    private lateinit var localMovieRecommendationsRepository: LocalMovieRecommendationsRepository
    private lateinit var likedMoviesDbRepository: LikedMoviesDbRepository
    private lateinit var savedStateHandle: SavedStateHandle

    private val mapOfGenresWithPage = mapOf(
        "28" to 1
    )

    val listOfRecommendedMovies = listOf(
        Movie(
            id = 54,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02",
            voteAverage = 7.6,
            genres = dummyGenresList
        )
    )
    private val listOfLikedMovies = listOf(
        LikedMovie(
            id = 321,
            title = "Batman",
            poster = null,
            releaseDate = "2021-06-02",
            voteAverage = 7.0,
            genres = dummyGenresList,
            nextPage = 1
        )
    )

    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setup() {
        genresPreferences = mockk()
        remoteMovieRecommendationsRepository = mockk()
        localMovieRecommendationsRepository = mockk()
        likedMoviesDbRepository = mockk()
        savedStateHandle = SavedStateHandle()
        sut = RecommendationsViewModel(
            savedStateHandle = savedStateHandle,
            genresPreferences = genresPreferences,
            remoteMovieRecommendationsRepository = remoteMovieRecommendationsRepository,
            localMovieRecommendationsRepository = localMovieRecommendationsRepository,
            likedMoviesDbRepository = likedMoviesDbRepository,
            ioDispatcher = replaceMainDispatcherRule.testDispatcher
        )

        every { genresPreferences.getGenresWithPage() } answers { emptyMap() }
        every { genresPreferences.savePagesForGenres(any()) } answers { }
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(
                any(),
                any()
            )
        } answers {
            Result.Success(emptyList())
        }
        coEvery {
            remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(
                any(),
                any()
            )
        } answers {
            Result.Success(emptyList())
        }
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
            Result.Success(
                emptyList()
            )
        }
        coEvery { localMovieRecommendationsRepository.addRecommendedMovies(any()) } answers {
            Result.Success(
                Unit
            )
        }
        coEvery { likedMoviesDbRepository.updatePageForLikedMovie(any(), any()) } answers {
            Result.Success(Unit)
        }
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(
                emptyList()
            )
        }
        coEvery { likedMoviesDbRepository.updatePageForLikedMovie(any(), any()) } answers {
            Result.Success(Unit)
        }
        coEvery { likedMoviesDbRepository.updatePageForAllLikedMovies(any()) } answers {
            Result.Success(Unit)
        }
        coEvery { likedMoviesDbRepository.addLikedMovie(any()) } answers {
            Result.Success(Unit)
        }
        coEvery { likedMoviesDbRepository.deleteLikedMovie(any()) } answers {
            Result.Success(Unit)
        }
    }

    // INIT
    @Test
    fun init_getLikedMoviesInvokedOnce() = runTest {
        // Arrange
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
            Result.Success(
                listOfRecommendedMovies
            )
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ likedMoviesDbRepository.getLikedMovies()}
    }


    @Test
    fun init_getGenresWithPageInvokedOnce() = runTest {
        // Arrange
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
            Result.Success(
                listOfRecommendedMovies
            )
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ genresPreferences.getGenresWithPage()}
    }

    @Test
    fun init_getRecommendedMoviesInvokedOnce() = runTest {
        // Arrange
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
            Result.Success(
                listOfRecommendedMovies
            )
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ localMovieRecommendationsRepository.getRecommendedMovies()}
    }

    // LOAD CACHED RECOMMENDATIONS
    @Test
    fun loadCachedRecommendations_successAndRecommendationsNotEmptyInLocalDb_recommendedMovieUpdatedWithCorrectMovie() =
        runTest {
            // Arrange
            coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
                Result.Success(
                    listOfRecommendedMovies
                )
            }

            sut.state.test {
                // Act
                val recommendedMovie = awaitItem().recommendedMovie

                // Assert
                assertThat(recommendedMovie).isEqualTo(listOfRecommendedMovies.first())
                ensureAllEventsConsumed()
            }
        }

    @Test
    fun loadCachedRecommendations_successAndRecommendationsAreEmptyInLocalDb_remoteMovieRecommendationsRepositoryFunctionInvokedOnce() =
        runTest {
            // Arrange
            coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
                Result.Success(
                    emptyList()
                )
            }

            coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
                Result.Success(
                    listOfLikedMovies
                )
            }
            coEvery {
                remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(
                    any(),
                    any()
                )
            } answers {
                Result.Success(listOfRecommendedMovies)
            }

            sut.state.test {
                // Act
                cancelAndIgnoreRemainingEvents()
            }
            // Assert
            coVerify(exactly = 1) {
                remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any())
            }
        }

    @Test
    fun loadCachedRecommendations_success_loadingStateChangesCorrectly() = runTest {
        // Arrange
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } coAnswers {
            delay(500)
            Result.Success(
                listOfRecommendedMovies
            )
        }

        sut.state.test {
            // Act
            val updatedState = awaitItem()
            MatcherAssert.assertThat(updatedState.isLoading, `is`(true))

            val successState = awaitItem()
            MatcherAssert.assertThat(successState.isLoading, `is`(false))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun loadCachedRecommendations_error_remoteMovieRecommendationsRepositoryFunctionInvokedOnce() =
        runTest {
            // Arrange
            coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
                Result.Error(
                    DataError.Local.UNKNOWN
                )
            }

            coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
                Result.Success(listOfLikedMovies)
            }

            coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
                Result.Success(listOfRecommendedMovies)
            }

            sut.state.test {
                // Act
                cancelAndIgnoreRemainingEvents()
            }
            // Assert
            coVerify(exactly = 1) {
                remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any())
            }
        }


    // FETCH NEW RECOMMENDATIONS
    @Test
    fun fetchNewRecommendations_likedMoviesAndLikedGenresAreEmpty_errorStateUpdated() = runTest{
        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.error).isNotNull()
        }
    }

    @Test
    fun fetchNewRecommendations_likedMoviesAndLikedGenresAreEmpty_loadingStateUpdatedWithFalse() = runTest{
        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isFalse()
        }
    }
    @Test
    fun fetchNewRecommendations_likedMoviesHaveNextPageEqualsTo1_getMoviesBasedOnMovieInvokedOnceWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(listOfLikedMovies[0].id, 1)}
    }


    @Test
    fun fetchNewRecommendations_likedMoviesHaveNextPageEqualsTo2_getMoviesBasedOnMovieInvokedOnceWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 2) })
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(listOfLikedMovies[0].id, 2)}
    }

    @Test
    fun fetchNewRecommendations_likedMoviesHaveNextPageEqualsTo3_getMoviesBasedOnMovieNotInvoked() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 0){ remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(listOfLikedMovies[0].id, 3)}
        coVerify(exactly = 0){ remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any())}
    }

    @Test
    fun fetchNewRecommendations_likedMoviesHaveNextPageEqualsTo3_getMoviesBasedOnGenreInvokedWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any())}
    }

    @Test
    fun fetchNewRecommendations_likedGenresHaveNextPageEqualsTo1_getMoviesBasedOnGenreInvokedWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(mapOfGenresWithPage.keys.first(), 1)}
    }

    @Test
    fun fetchNewRecommendations_likedGenresHaveNextPageEqualsTo2_getMoviesBasedOnGenreInvokedWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
           mapOf(mapOfGenresWithPage.keys.first() to 2 )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(mapOfGenresWithPage.keys.first(), 2)}
    }

    @Test
    fun fetchNewRecommendations_likedGenresHaveNextPageEqualsTo14_getMoviesBasedOnGenreInvokedWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOf(mapOfGenresWithPage.keys.first() to 14 )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(mapOfGenresWithPage.keys.first(), 14)}
    }

    @Test
    fun fetchNewRecommendations_likedGenresHaveNextPageEqualsMoreThan14_getMoviesBasedOnGenreNotInvoked() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOf(mapOfGenresWithPage.keys.first() to 15 )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 0){ remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(mapOfGenresWithPage.keys.first(), 15)}
        coVerify(exactly = 0){ remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any())}
    }

    @Test
    fun fetchNewRecommendations_firstLikeGenreHaveNextPageEqualsTo3AndSecondEqualsTo2_getMoviesBasedOnGenreInvokedWithSecondGenre() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOf(
                mapOfGenresWithPage.keys.first() to 3,
                "3" to 2,
            )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ remoteMovieRecommendationsRepository.getMoviesBasedOnGenre("3", 2)}
    }

    @Test
    fun fetchNewRecommendations_likedMoviesAndGenresDoNotHaveAvailablePages_resetLikedMoviesPagesInvokedOnceWithCorrectPage() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOf(mapOfGenresWithPage.keys.first() to 15 )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ likedMoviesDbRepository.updatePageForAllLikedMovies(1)}
    }

    @Test
    fun fetchNewRecommendations_likedMoviesAndGenresDoNotHaveAvailablePages_savePagesForGenresInvokedOnceWithCorrectPages() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOf(mapOfGenresWithPage.keys.first() to 15 )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ genresPreferences.savePagesForGenres(List(listOfLikedMovies.size) { 1 })}
    }

    @Test
    fun fetchNewRecommendations_likedMoviesAndGenresDoNotHaveAvailablePages_resetPagesAndGetMoviesBasedOnMovieInvokedOnceWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies.map { it.copy(nextPage = 3) })
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOf(mapOfGenresWithPage.keys.first() to 15 )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){ remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(listOfLikedMovies.first().id, 1)}
    }

    // FETCH RECOMMENDED MOVIES FROM API BY MOVIE
    @Test
    fun fetchRecommendedMoviesFromApiByMovie_successAndResultListIsNotEmpty_recommendedMovieStateUpdatedWithCorrectMovie() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.recommendedMovie).isEqualTo(listOfRecommendedMovies.first())
            ensureAllEventsConsumed()
        }
    }


    @Test
    fun fetchRecommendedMoviesFromApiByMovie_successAndResultListIsNotEmpty_addRecommendedMoviesToDbInvokedOnceWithCorrectMovies() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){localMovieRecommendationsRepository.addRecommendedMovies(listOfRecommendedMovies)}
    }

    @Test
    fun fetchRecommendedMoviesFromApiByMovie_successAndResultListIsNotEmpty_addRecommendedMoviesToDbInvokedOnceWithCorrectMoviesOrderById() = runTest{
        // Arrange
        val movieWithSmallId = Movie(
            id = 5,
            title = "Batman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2022-04-02",
            voteAverage = 8.0,
            genres = dummyGenresList
        )
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies + movieWithSmallId)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){localMovieRecommendationsRepository.addRecommendedMovies(listOf(movieWithSmallId) + listOfRecommendedMovies)}
    }

    @Test
    fun fetchRecommendedMoviesFromApiByMovie_successAndResultListIsNotEmptyWithDuplicatedMoviesFromLikedMovies_addRecommendedMoviesToDbInvokedOnceWithCorrectFilteredMovies() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies + listOfLikedMovies.map { Movie(
                id = it.id,
                title = it.title,
                poster = it.poster,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage,
                genres = it.genres
            ) })
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){localMovieRecommendationsRepository.addRecommendedMovies(listOfRecommendedMovies)}
    }


    @Test
    fun fetchRecommendedMoviesFromApiByMovie_successAndResultListIsNotEmptyWithDuplicatedMoviesFromLikedMovies_updatePageForLikedMovieInvokedOnceWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies + listOfLikedMovies.map { Movie(
                id = it.id,
                title = it.title,
                poster = it.poster,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage,
                genres = it.genres
            ) })
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){likedMoviesDbRepository.updatePageForLikedMovie(listOfLikedMovies.first().id, listOfLikedMovies.first().nextPage+1)}
    }

    @Test
    fun fetchRecommendedMoviesFromApiByMovie_successAndResultListIsNotEmpty_isLoadingStateUpdatedWithFalse() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies + listOfLikedMovies.map { Movie(
                id = it.id,
                title = it.title,
                poster = it.poster,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage,
                genres = it.genres
            ) })
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isEqualTo(false)
        }
    }

    @Test
    fun fetchRecommendedMoviesFromApiByMovie_successAndResultListIsEmpty_updatePageForLikedMovieInvokedOnceWithCorrectPage() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }
        // Assert
        coVerify(exactly = 1){likedMoviesDbRepository.updatePageForLikedMovie(any(), 3)}
    }

    @Test
    fun fetchRecommendedMoviesFromApiByMovie_error_errorStateUpdatedWithCorrectMessage() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }

        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Error(DataError.Network.UNKNOWN)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.error).isEqualTo(DataError.Local.UNKNOWN.asUiText())
        }
    }

    @Test
    fun fetchRecommendedMoviesFromApiByMovie_error_isLoadingStateUpdatedWithFalse() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Error(DataError.Local.UNKNOWN)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isEqualTo(false)
        }
    }

    //FETCH RECOMMENDED MOVIES FROM API BY GENRE
    @Test
    fun fetchRecommendedMoviesFromApiByGenre_success_recommendedMovieStateUpdatedWithCorrectMovie() = runTest{
        // Arrange
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.recommendedMovie).isEqualTo(listOfRecommendedMovies.first())
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun fetchRecommendedMoviesFromApiByGenre_success_addRecommendedMoviesToDbInvokedOnceWithCorrectMovies() = runTest{
        // Arrange
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){localMovieRecommendationsRepository.addRecommendedMovies(listOfRecommendedMovies)}
    }

    @Test
    fun fetchRecommendedMoviesFromApiByGenre_success_isLoadingStateUpdatedWithFalse() = runTest{
        // Arrange
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isEqualTo(false)
        }
    }

    @Test
    fun fetchRecommendedMoviesFromApiByGenre_success_addRecommendedMoviesToDbInvokedOnceWithCorrectMoviesOrderById() = runTest{
        // Arrange
        val movieWithSmallId = Movie(
            id = 5,
            title = "Batman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2022-04-02",
            voteAverage = 8.0,
            genres = dummyGenresList
        )
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies + movieWithSmallId)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){localMovieRecommendationsRepository.addRecommendedMovies(listOf(movieWithSmallId) + listOfRecommendedMovies)}
    }

    @Test
    fun fetchRecommendedMoviesFromApiByGenre_successAndResultListWithDuplicatedMoviesFromLikedMovies_addRecommendedMoviesToDbInvokedOnceWithCorrectFilteredMovies() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies + listOfLikedMovies.map { Movie(
                id = it.id,
                title = it.title,
                poster = it.poster,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage,
                genres = it.genres
            ) })
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){localMovieRecommendationsRepository.addRecommendedMovies(listOfRecommendedMovies)}
    }

    @Test
    fun fetchRecommendedMoviesFromApiByGenre_successAndResultListWithDuplicatedMoviesFromLikedMovies_savePagesForGenresInvokedOnceWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies + listOfLikedMovies.map { Movie(
                id = it.id,
                title = it.title,
                poster = it.poster,
                releaseDate = it.releaseDate,
                voteAverage = it.voteAverage,
                genres = it.genres
            ) })
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){genresPreferences.savePagesForGenres(listOf(2))}
    }

    @Test
    fun fetchRecommendedMoviesFromApiByGenre_error_errorStateUpdatedWithCorrectMessage() = runTest{
        // Arrange
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Error(DataError.Network.UNKNOWN)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.error).isEqualTo(DataError.Local.UNKNOWN.asUiText())
        }
    }

    @Test
    fun fetchRecommendedMoviesFromApiByGenre_error_isLoadingStateUpdatedWithFalse() = runTest{
        // Arrange
        every { genresPreferences.getGenresWithPage() } answers {
            mapOfGenresWithPage
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any()) } answers {
            Result.Error(DataError.Network.UNKNOWN)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isLoading).isEqualTo(false)
        }
    }

    // GET LIKED MOVIES
    @Test
    fun getLikedMovies_success_fetchRecommendedMoviesFromApiByMovieInvokedOnceWithCorrectParameters() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 1){remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(listOfLikedMovies.first().id, listOfLikedMovies.first().nextPage)}
    }
    @Test
    fun getLikedMovies_error_fetchingNotInvoked() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Error(DataError.Local.UNKNOWN)
        }

        sut.state.test{
            // Act
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        coVerify(exactly = 0){remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any())}
        coVerify(exactly = 0){remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(any(), any())}
    }

    // SET IS MOVIE SCRATCHED STATE TO TRUE
    @Test
    fun setIsMovieScratchedStateToTrue_isMovieScratchedStateUpdatedWithTrue() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            cancelAndIgnoreRemainingEvents()
        }
        // Act
        sut.onAction(RecommendationsAction.OnImageScratched)

        // Assert
        assertThat(sut.state.value.isMovieScratched).isEqualTo(true)
    }

    @Test
    fun setIsMovieScratchedStateToTrue_isMovieScratchedSaveStateHandleUpdatedWithTrue() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            cancelAndIgnoreRemainingEvents()
        }
        // Act
        sut.onAction(RecommendationsAction.OnImageScratched)

        // Assert
        assertThat(savedStateHandle[IS_MOVIE_SCRATCHED]!! as Boolean).isEqualTo(true)
    }

    // ON HEARTH CLICKED
    @Test
    fun onHeartClicked_movieIsNotLiked_isMovieLikedUpdatedWithTrue() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            skipItems(1)

            // Act
            sut.onAction(RecommendationsAction.OnHeartClicked)
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isMovieLiked).isEqualTo(true)

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun onHeartClicked_movieIsLiked_isMovieLikedUpdatedWithFalse() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            skipItems(1)
            sut.onAction(RecommendationsAction.OnHeartClicked)
            skipItems(1)

            // Act
            sut.onAction(RecommendationsAction.OnHeartClicked)
            val updatedState = awaitItem()

            // Assert
            assertThat(updatedState.isMovieLiked).isEqualTo(false)

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun onHeartClicked_movieIsLiked_deleteLikedMovieFromLocalDbInvokedOnceWithCorrectMovie() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            skipItems(1)
            sut.onAction(RecommendationsAction.OnHeartClicked)
            skipItems(1)

            // Act
            sut.onAction(RecommendationsAction.OnHeartClicked)
            skipItems(1)

            ensureAllEventsConsumed()
        }
        // Assert
        coVerify(exactly = 1){likedMoviesDbRepository.deleteLikedMovie(listOfRecommendedMovies.first())}
    }

    @Test
    fun onHeartClicked_movieIsNotLiked_addLikedMovieToLocalDbInvokedOnceWithCorrectMovie() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            skipItems(1)
            // Act
            sut.onAction(RecommendationsAction.OnHeartClicked)
            skipItems(1)

            ensureAllEventsConsumed()
        }
        // Assert
        coVerify(exactly = 1){likedMoviesDbRepository.addLikedMovie(listOfRecommendedMovies.first())}
    }

    @Test
    fun onHeartClicked_movieIsLikedAndError_eventChannelUpdatedWithCorrectError() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }
        coEvery { likedMoviesDbRepository.deleteLikedMovie(any()) } answers {
            Result.Error(DataError.Local.UNKNOWN)
        }

        sut.state.test{
            skipItems(1)
            sut.onAction(RecommendationsAction.OnHeartClicked)
            skipItems(1)
            ensureAllEventsConsumed()
        }

        sut.events.test{
            // Act
            sut.onAction(RecommendationsAction.OnHeartClicked)

            val emission = awaitItem()

            // Assert
            assertTrue(emission is RecommendationsEvent.Error && emission.error == DataError.Local.UNKNOWN.asUiText())
        }
    }

    @Test
    fun onHeartClicked_movieIsNotLikedAndError_eventChannelUpdatedWithCorrectError() = runTest{
        // Arrange
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOfLikedMovies)
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }
        coEvery { likedMoviesDbRepository.addLikedMovie(any()) } answers {
            Result.Error(DataError.Local.UNKNOWN)
        }

        sut.state.test{
            skipItems(1)
            ensureAllEventsConsumed()
        }

        sut.events.test{
            // Act
            sut.onAction(RecommendationsAction.OnHeartClicked)

            val emission = awaitItem()

            // Assert
            assertTrue(emission is RecommendationsEvent.Error && emission.error == DataError.Local.UNKNOWN.asUiText())
        }
    }
}