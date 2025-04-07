@file:OptIn(ExperimentalCoroutinesApi::class)

package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.adamdawi.popcornpicks.core.KoinTestRule
import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.local.OnBoardingManager
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.ERROR_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.FINISH_FAB_DISABLED
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.FINISH_FAB_ENABLED
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LAZY_MOVIES_GRID
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LOADING_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_ITEM
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_ITEM_NOT_SELECTED
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_ITEM_SELECTED
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

class MovieChooseScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository: RemoteMovieRecommendationsRepository = mockk(relaxed = true)
    private val genresPreferences: GenresPreferences = mockk(relaxed = true)
    private val onBoardingManager: OnBoardingManager = mockk(relaxed = true)
    private val likedMoviesDbRepository: LikedMoviesDbRepository = mockk(relaxed = true)

    private val instrumentedTestModule = module {
        factory { repository }
        factory { genresPreferences }
        factory { onBoardingManager }
        factory { likedMoviesDbRepository }
        factory { Dispatchers.IO }
        viewModel { MovieChooseViewModel(get(), get(), get(), get(), get()) }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(instrumentedTestModule)
    )

    private val viewModel by inject<MovieChooseViewModel>(clazz = MovieChooseViewModel::class.java)

    @Before
    fun setup() {
        every { genresPreferences.getGenres() } answers { listOf("28") }
        // for every genre 3 pages with movies are fetched
        coEvery { repository.getMoviesBasedOnGenre("28", 1) } answers {
            Result.Success(
                listOf(
                    Movie(
                        id = 1,
                        title = "Spiderman",
                        poster = null,
                        releaseDate = "2020-04-02",
                        voteAverage = 7.6,
                        genres = dummyGenresList
                    )
                )
            )
        }
        coEvery { repository.getMoviesBasedOnGenre("28", 2) } answers {
            Result.Success(
                listOf(
                    Movie(
                        id = 2,
                        title = "Batman",
                        poster = null,
                        releaseDate = "2022-06-07",
                        voteAverage = 8.8,
                        genres = dummyGenresList
                    )
                )
            )
        }
        coEvery { repository.getMoviesBasedOnGenre("28", 3) } answers {
            Result.Success(
                listOf(
                    Movie(
                        id = 3,
                        title = "Thor",
                        poster = null,
                        releaseDate = "2025-07-04",
                        voteAverage = 6.8,
                        genres = dummyGenresList
                    )
                )
            )
        }
    }

    @Test
    fun movieChooseScreen_successWithEmptyMovieList_moviesNotDisplayed() {
        coEvery { repository.getMoviesBasedOnGenre(any(), any()) } answers { Result.Success(emptyList()) }
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onAllNodesWithTag(MOVIE_ITEM).assertCountEquals(0)
    }


    @Test
    fun movieChooseScreen_successWithEmptyMovieList_errorScreenDisplayed() {
        coEvery { repository.getMoviesBasedOnGenre(any(), any()) } answers { Result.Success(emptyList()) }
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(ERROR_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_successWithEmptyMovieList_errorScreenDisplayedWithCorrectMessage() {
        coEvery { repository.getMoviesBasedOnGenre(any(), any()) } answers { Result.Success(emptyList()) }
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText("Something went wrong").assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_successWithNotEmptyMovieList_correctNumberOfMoviesDisplayed() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM).assertCountEquals(3)
    }

    @Test
    fun movieChooseScreen_successWithNotEmptyMovieList_correctTitleAndReleaseDateOfMoviesDisplayed() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithText("Spiderman").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("2020").assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText("Batman").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("2022").assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithText("Thor").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("2025").assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_isLoading_loadingScreenDisplayed() {
        coEvery { repository.getMoviesBasedOnGenre(any(), any()) } coAnswers {
            delay(500)
            Result.Error(DataError.Network.UNKNOWN)
        }

        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(LOADING_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_error_errorScreenDisplayed() {
        coEvery { repository.getMoviesBasedOnGenre(any(), any()) } answers { Result.Error(DataError.Network.UNKNOWN) }

        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag(ERROR_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_error_errorScreenDisplayedWithCorrectMessage() {
        coEvery { repository.getMoviesBasedOnGenre(any(), any()) } answers { Result.Error(DataError.Network.UNKNOWN) }

        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText(DataError.Network.UNKNOWN.asUiText()).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_zeroMoviesSelected_finishButtonIsDisabled() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription(FINISH_FAB_DISABLED).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_zeroMoviesSelected_zeroMoviesInSelectedMoviesState() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        assertTrue(viewModel.state.value.selectedMovies.isEmpty())
    }

    @Test
    fun movieChooseScreen_oneMovieSelected_finishButtonIsDisabled() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription("Spiderman").performClick()

        composeTestRule.onNodeWithContentDescription(FINISH_FAB_DISABLED).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_oneMovieSelected_oneMovieInSelectedMoviesState() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription("Spiderman").performClick()

        assertTrue(viewModel.state.value.selectedMovies.size == 1)
    }

    @Test
    fun movieChooseScreen_oneMovieSelected_correctMovieIsSelected() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription("Spiderman").performClick()

        composeTestRule.onNodeWithContentDescription(MOVIE_ITEM_SELECTED + "Spiderman")
        composeTestRule.onNodeWithContentDescription(MOVIE_ITEM_NOT_SELECTED + "Batman")
        composeTestRule.onNodeWithContentDescription(MOVIE_ITEM_NOT_SELECTED + "Thor")
    }

    @Test
    fun movieChooseScreen_twoMoviesSelected_finishButtonIsDisabled() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription("Spiderman").performClick()
        composeTestRule.onNodeWithContentDescription("Batman").performClick()

        composeTestRule.onNodeWithContentDescription(FINISH_FAB_DISABLED).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_twoMoviesSelected_twoMoviesInSelectedMoviesState() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription("Spiderman").performClick()
        composeTestRule.onNodeWithContentDescription("Batman").performClick()

        assertTrue(viewModel.state.value.selectedMovies.size == 2)
    }

    @Test
    fun movieChooseScreen_twoMoviesSelected_correctTwoMoviesAreSelected() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription("Spiderman").performClick()
        composeTestRule.onNodeWithContentDescription("Batman").performClick()

        composeTestRule.onNodeWithContentDescription(MOVIE_ITEM_SELECTED + "Spiderman")
        composeTestRule.onNodeWithContentDescription(MOVIE_ITEM_SELECTED + "Batman")
        composeTestRule.onNodeWithContentDescription(MOVIE_ITEM_NOT_SELECTED + "Thor")
    }

    @Test
    fun movieChooseScreen_threeMoviesSelected_finishButtonIsEnabled() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription("Spiderman").performClick()
        composeTestRule.onNodeWithContentDescription("Batman").performClick()
        composeTestRule.onNodeWithContentDescription("Thor").performClick()

        composeTestRule.onNodeWithContentDescription(FINISH_FAB_ENABLED).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_threeMoviesSelected_threeMoviesInSelectedMoviesState() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription("Spiderman").performClick()
        composeTestRule.onNodeWithContentDescription("Batman").performClick()
        composeTestRule.onNodeWithContentDescription("Thor").performClick()

        assertTrue(viewModel.state.value.selectedMovies.size == 3)
    }

    @Test
    fun movieChooseScreen_threeMoviesSelected_correctThreeMoviesAreSelected() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithContentDescription("Spiderman").performClick()
        composeTestRule.onNodeWithContentDescription("Batman").performClick()
        composeTestRule.onNodeWithContentDescription("Thor").performClick()

        composeTestRule.onNodeWithContentDescription(MOVIE_ITEM_SELECTED + "Spiderman")
        composeTestRule.onNodeWithContentDescription(MOVIE_ITEM_SELECTED + "Batman")
        composeTestRule.onNodeWithContentDescription(MOVIE_ITEM_SELECTED + "Thor")
    }

    @Test
    fun movieChooseScreen_withoutScroll_finishButtonTextIsDisplayed() {
        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithText("Finish", useUnmergedTree = true).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieChooseScreen_scrollDown_finishButtonTextIsNotDisplayed() {
        coEvery { repository.getMoviesBasedOnGenre(any(), 1) } answers { Result.Success(
            dummyMovieList
        ) }

        coEvery { repository.getMoviesBasedOnGenre(any(), 2) } answers { Result.Success(
            emptyList()
        ) }

        coEvery { repository.getMoviesBasedOnGenre(any(), 3) } answers { Result.Success(
            emptyList()
        ) }

        composeTestRule.setContent {
            MovieChooseScreen(
                onFinishClick = {},
                viewModel = viewModel
            )
        }
        composeTestRule.onNodeWithTag(LAZY_MOVIES_GRID)
            .performTouchInput {
                swipeUp()
            }

        composeTestRule.onNodeWithText("Finish", useUnmergedTree = true).assertIsNotDisplayed()
    }
}