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
        "28" to 1,
        "12" to 1,
        "878" to 1
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
    }

    @Test
    fun loadCachedRecommendations_successAndRecommendationsAvailableInLocalDb_recommendedMovieUpdatedWithCorrectMovie() =
        runTest {
            // Arrange
            coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
                Result.Success(
                    listOfRecommendedMovies
                )
            }
            every { genresPreferences.getGenresWithPage() } answers { emptyMap() }
            coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
                Result.Success(
                    listOfLikedMovies
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
    fun loadCachedRecommendations_successAndRecommendationsAreEmptyInLocalDb_remoteMovieRecommendationsRepositoryFunctionInvokedOnce() = runTest{
        // Arrange
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
            Result.Success(
                emptyList()
            )
        }
        coEvery { localMovieRecommendationsRepository.addRecommendedMovies(any()) } answers { Result.Success(Unit) }
        every { genresPreferences.getGenresWithPage() } answers { emptyMap() }
        coEvery { likedMoviesDbRepository.updatePageForLikedMovie(any(), any()) } answers {
            Result.Success(Unit)
        }
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(
                listOfLikedMovies
            )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            skipItems(1)
            ensureAllEventsConsumed()
        }
        // Assert
        coVerify(exactly = 1) {
            remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any())
        }
    }

    @Test
    fun loadCachedRecommendations_success_loadingStateChangesCorrectly() = runTest{
        // Arrange
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } coAnswers {
            delay(500)
            Result.Success(
                emptyList()
            )
        }
        coEvery { localMovieRecommendationsRepository.addRecommendedMovies(any()) } answers { Result.Success(Unit) }
        every { genresPreferences.getGenresWithPage() } answers { emptyMap() }
        coEvery { likedMoviesDbRepository.updatePageForLikedMovie(any(), any()) } answers {
            Result.Success(Unit)
        }
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(
                listOfLikedMovies
            )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            val updatedState = awaitItem()
            MatcherAssert.assertThat(updatedState.isLoading, `is`(true))

            val successState = awaitItem()
            MatcherAssert.assertThat(successState.isLoading, `is`(false))

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun loadCachedRecommendations_error_remoteMovieRecommendationsRepositoryFunctionInvokedOnce() = runTest{
        // Arrange
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
            Result.Error(
                DataError.Local.UNKNOWN
            )
        }
        coEvery { localMovieRecommendationsRepository.addRecommendedMovies(any()) } answers { Result.Success(Unit) }
        every { genresPreferences.getGenresWithPage() } answers { emptyMap() }
        coEvery { likedMoviesDbRepository.updatePageForLikedMovie(any(), any()) } answers {
            Result.Success(Unit)
        }
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(
                listOfLikedMovies
            )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(listOfRecommendedMovies)
        }

        sut.state.test{
            // Act
            skipItems(1)
            ensureAllEventsConsumed()
        }
        // Assert
        coVerify(exactly = 1) {
            remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any())
        }
    }
}