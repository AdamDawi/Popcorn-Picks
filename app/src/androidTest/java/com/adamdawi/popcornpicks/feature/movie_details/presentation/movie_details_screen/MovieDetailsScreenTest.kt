package com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.SavedStateHandle
import com.adamdawi.popcornpicks.utils.KoinTestRule
import com.adamdawi.popcornpicks.core.data.dummy.dummyDetailedMovie
import com.adamdawi.popcornpicks.core.domain.util.Constants.NavArguments.MOVIE_ID
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.ERROR_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LOADING_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.movie_details.domain.remote.MovieDetailsRepository
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

class MovieDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val savedStateHandle = SavedStateHandle(mapOf(MOVIE_ID to "1"))
    private val movieDetailsRepository: MovieDetailsRepository = mockk(relaxed = true)

    private val instrumentedTestModule = module {
        factory { movieDetailsRepository }
        factory { savedStateHandle }
        factory { Dispatchers.IO }
        viewModel { MovieDetailsViewModel(get(), get(), get()) }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(instrumentedTestModule)
    )
    private val viewModel by inject<MovieDetailsViewModel>(clazz = MovieDetailsViewModel::class.java)

    @Before
    fun setup() {
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } answers {
            Result.Success(
                dummyDetailedMovie
            )
        }
    }

    @Test
    fun movieDetailsScreen_success_correctTitleIsDisplayed() {
        composeTestRule.setContent {
            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onNodeWithText("Spider-Man: No Way Home").assertExists().assertIsDisplayed()
    }


    @Test
    fun movieDetailsScreen_success_correctReleaseDateIsDisplayed() {
        composeTestRule.setContent {
            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onNodeWithText("2021-12").assertExists().assertIsDisplayed()
    }

    @Test
    fun movieDetailsScreen_success_correctRatingIsDisplayed() {
        composeTestRule.setContent {
            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onNodeWithText("7.60/10").assertExists().assertIsDisplayed()
    }

    @Test
    fun movieDetailsScreen_success_correctGenreIsDisplayed() {
        composeTestRule.setContent {
            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onNodeWithText("Action").assertExists().assertIsDisplayed()
    }

    @Test
    fun movieDetailsScreen_success_correctRuntimeIsDisplayed() {
        composeTestRule.setContent {
            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onNodeWithText("2h 28m").assertExists().assertIsDisplayed()
    }

    @Test
    fun movieDetailsScreen_success_correctOverviewIsDisplayed() {
        composeTestRule.setContent {
            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onNodeWithText("Peter Parker is unmasked and no longer able to separate his normal life from the high-stakes of being a super-hero. When he asks for help from Doctor Strange the stakes become even more dangerous, forcing him to discover what it truly means to be Spider-Man.")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun movieDetailsScreen_isLoading_loadingScreenDisplayed() {
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } coAnswers {
            delay(500)
            Result.Error(DataError.Network.UNKNOWN)
        }

        composeTestRule.setContent {
            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }

        composeTestRule.onNodeWithTag(LOADING_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieDetailsScreen_error_errorScreenIsDisplayed() {
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } answers { Result.Error(DataError.Network.UNKNOWN) }
        composeTestRule.setContent {
            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onNodeWithTag(ERROR_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieDetailsScreen_error_correctErrorInErrorScreenDisplayed() {
        coEvery { movieDetailsRepository.getDetailedMovie(any()) } answers { Result.Error(DataError.Network.UNKNOWN) }
        composeTestRule.setContent {
            MovieDetailsScreen(
                viewModel = viewModel,
                onNavigateBack = {}
            )
        }
        composeTestRule.onNodeWithText(DataError.Network.UNKNOWN.asUiText()).assertExists().assertIsDisplayed()
    }
}