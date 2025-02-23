package com.adamdawi.popcornpicks.feature.recommendations.presentation

import app.cash.turbine.test
import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.LocalMovieRecommendationsRepository
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

    private val mapOfGenresWithPage = mapOf(
        "28" to 1
    )

    val listOfRecommendedMovies = listOf(
        Movie(
            id = 1,
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
        sut = RecommendationsViewModel(
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
    fun fetchNewRecommendations_likedMoviesAndGenresDoNotHaveAvailablePages_getMoviesBasedOnMovieInvokedOnceWithCorrectParameters() = runTest{
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
}