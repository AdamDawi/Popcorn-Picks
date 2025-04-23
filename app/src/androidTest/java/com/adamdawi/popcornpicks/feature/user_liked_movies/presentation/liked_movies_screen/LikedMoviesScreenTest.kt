package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.adamdawi.popcornpicks.utils.KoinTestRule
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.ERROR_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LOADING_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.POSTER_IMAGE
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.TEXT_CHIP
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class LikedMoviesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val likedMoviesDbRepository: LikedMoviesDbRepository = mockk(relaxed = true)

    private val instrumentedTestModule = module {
        factory { likedMoviesDbRepository }
        factory { Dispatchers.IO }
        viewModel { LikedMoviesViewModel(get(), get()) }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(instrumentedTestModule)
    )

    private val viewModel by inject<LikedMoviesViewModel>(clazz = LikedMoviesViewModel::class.java)

    @Before
    fun setup() {
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(listOf(
                LikedMovie(
                    id = 1,
                    title = "Spider-Man: No Way Home",
                    poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
                    genres = listOf(
                        Genre(id = 28, name = "Action"),
                        Genre(id = 12, name = "Adventure"),
                        Genre(id = 878, name = "Science Fiction")
                    ),
                    releaseDate = "2021-12-15",
                    voteAverage = 7.834,
                    nextPage = 1
                ),
                LikedMovie(
                    id = 2,
                    title = "Batman",
                    poster = null,
                    genres = listOf(
                        Genre(id = 36, name = "History"),
                        Genre(id = 27, name = "Horror"),
                        Genre(id = 10402, name = "Music")
                    ),
                    releaseDate = "2024-10-10",
                    voteAverage = 8.86,
                    nextPage = 1
                )
            ))
        }
    }

    @Test
    fun likedMoviesScreen_successWithEmptyMoviesList_correctTextIsDisplayed(){
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(emptyList())
        }

        composeTestRule.setContent {
            LikedMoviesScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }

        composeTestRule.onNodeWithText("No movies available").assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMoviesScreen_successWithNotEmptyMoviesList_correctMoviesAreDisplayed(){
        composeTestRule.setContent {
            LikedMoviesScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }

        // FIRST MOVIE
        composeTestRule.onNodeWithText("Spider-Man: No Way Home (2021)").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("2021").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Action").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Adventure").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Science Fiction").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("7.8").assertExists().assertIsDisplayed()


        // SECOND MOVIE
        composeTestRule.onNodeWithText("Batman (2024)").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("2024").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("History").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Horror").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Music").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("8.9").assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMoviesScreen_successWithNotEmptyMoviesList_correctNumberOfTextChipsAreDisplayed(){
        composeTestRule.setContent {
            LikedMoviesScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onAllNodesWithTag(TEXT_CHIP).assertCountEquals(8)
    }

    @Test
    fun likedMoviesScreen_successWithNotEmptyMoviesList_correctNumberOfPosterImageAreDisplayed(){
        composeTestRule.setContent {
            LikedMoviesScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onAllNodesWithTag(POSTER_IMAGE).assertCountEquals(2)
    }

    @Test
    fun likedMoviesScreen_isLoading_loadingScreenDisplayed() {
        coEvery { likedMoviesDbRepository.getLikedMovies() } coAnswers {
            delay(500)
            Result.Error(DataError.Local.UNKNOWN)
        }

        composeTestRule.setContent {
            LikedMoviesScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }

        composeTestRule.onNodeWithTag(LOADING_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMoviesScreen_error_errorScreenDisplayed() {
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers { Result.Error(DataError.Local.UNKNOWN) }

        composeTestRule.setContent {
            LikedMoviesScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }

        composeTestRule.onNodeWithTag(ERROR_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMoviesScreen_error_correctErrorInErrorScreenDisplayed() {
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers { Result.Error(DataError.Local.UNKNOWN) }

        composeTestRule.setContent {
            LikedMoviesScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }

        composeTestRule.onNodeWithText(DataError.Network.UNKNOWN.asUiText()).assertExists().assertIsDisplayed()
    }
}