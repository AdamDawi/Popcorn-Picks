package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.lifecycle.SavedStateHandle
import com.adamdawi.popcornpicks.core.KoinTestRule
import com.adamdawi.popcornpicks.core.data.dummy.dummyLikedMoviesList
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.CIRCLE_ICON_BUTTON
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.ERROR_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.IMAGE_SCRATCH
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LOADING_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_DETAILS_NOT_VISIBLE
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_DETAILS_VISIBLE
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.LocalMovieRecommendationsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class RecommendationsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val genresPreferences: GenresPreferences = mockk(relaxed = true)
    private val remoteMovieRecommendationsRepository: RemoteMovieRecommendationsRepository =
        mockk(relaxed = true)
    private val localMovieRecommendationsRepository: LocalMovieRecommendationsRepository =
        mockk(relaxed = true)
    private val likedMoviesDbRepository: LikedMoviesDbRepository = mockk(relaxed = true)
    private val savedStateHandle = SavedStateHandle()

    private val instrumentedTestModule = module {
        factory { genresPreferences }
        factory { remoteMovieRecommendationsRepository }
        factory { localMovieRecommendationsRepository }
        factory { likedMoviesDbRepository }
        factory { savedStateHandle }
        factory { Dispatchers.IO }
        viewModel { RecommendationsViewModel(get(), get(), get(), get(), get(), get()) }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(instrumentedTestModule)
    )

    private val viewModel by inject<RecommendationsViewModel>(clazz = RecommendationsViewModel::class.java)

    @Before
    fun setup() {
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
                dummyMovieList
            )
        }
        coEvery { localMovieRecommendationsRepository.addRecommendedMovies(any()) } answers {
            Result.Success(Unit)
        }
        coEvery { localMovieRecommendationsRepository.deleteRecommendedMovie(any()) } answers {
            Result.Success(Unit)
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

    // IMAGE SCRATCHING
    @Test
    fun recommendationsScreen_imageNotScratched_movieDetailsAreNotDisplayed() {
        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }
        composeTestRule.onNodeWithContentDescription(MOVIE_DETAILS_NOT_VISIBLE).assertExists()
    }


    @Test
    fun recommendationsScreen_imageScratched_movieDetailsAreDisplayed() {
        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }
        composeTestRule.scratchImage()
        composeTestRule.onNodeWithContentDescription(MOVIE_DETAILS_VISIBLE).assertExists()
    }

    @Test
    fun recommendationsScreen_imageScratched_correctMovieDetailsAreDisplayed() {
        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }
        composeTestRule.scratchImage()
        composeTestRule.onNodeWithText("Spiderman").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("2020 · Action/Adventure · 7.6/10").assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun recommendationsScreen_imageNotScratched_circleIconButtonsAreDisabled() {
        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }

        val buttons = composeTestRule.onAllNodesWithTag(CIRCLE_ICON_BUTTON)
        buttons.assertCountEquals(3)
        for (i in 0..<3) {
            val button = buttons[i]
            button.assertIsDisplayed().assertIsNotEnabled()
        }
    }

    @Test
    fun recommendationsScreen_imageScratched_circleIconButtonsAreEnabled() {
        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }
        composeTestRule.scratchImage()

        val buttons = composeTestRule.onAllNodesWithTag(CIRCLE_ICON_BUTTON)
        buttons.assertCountEquals(3)
        for (i in 0..<3) {
            val button = buttons[i]
            button.assertIsDisplayed().assertIsEnabled()
        }
    }

    @Test
    fun recommendationsScreen_imageScratchedThenRetry_circleIconButtonsAreDisabled() {
        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }
        composeTestRule.scratchImage()
        composeTestRule.onNodeWithContentDescription("Retry").performClick()

        val buttons = composeTestRule.onAllNodesWithTag(CIRCLE_ICON_BUTTON)
        buttons.assertCountEquals(3)
        for (i in 0..<3) {
            val button = buttons[i]
            button.assertIsDisplayed().assertIsNotEnabled()
        }
    }

    @Test
    fun recommendationsScreen_imageScratchedThenRetryThenImageScratched_circleIconButtonsAreEnabled() {
        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }
        composeTestRule.scratchImage()
        composeTestRule.onNodeWithContentDescription("Retry").performClick()
        composeTestRule.scratchImage()

        val buttons = composeTestRule.onAllNodesWithTag(CIRCLE_ICON_BUTTON)
        buttons.assertCountEquals(3)
        for (i in 0..<3) {
            val button = buttons[i]
            button.assertIsDisplayed().assertIsEnabled()
        }
    }

    @Test
    fun recommendationsScreen_isLoading_loadingScreenDisplayed() {
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } coAnswers {
            delay(500)
            Result.Error(DataError.Local.UNKNOWN)
        }

        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }

        composeTestRule.onNodeWithTag(LOADING_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun genresScreen_error_errorScreenDisplayed() {
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(
                dummyLikedMoviesList
            )
        }
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
            Result.Success(
                emptyList()
            )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Error(DataError.Network.UNKNOWN)
        }

        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }

        composeTestRule.onNodeWithTag(ERROR_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun genresScreen_error_correctErrorInErrorScreenDisplayed() {
        coEvery { likedMoviesDbRepository.getLikedMovies() } answers {
            Result.Success(
                dummyLikedMoviesList
            )
        }
        coEvery { localMovieRecommendationsRepository.getRecommendedMovies() } answers {
            Result.Success(
                emptyList()
            )
        }
        coEvery { remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Error(DataError.Network.UNKNOWN)
        }

        composeTestRule.setContent {
            RecommendationsScreen(
                viewModel = viewModel,
                onNavigateToProfile = {},
                onNavigateToMovieDetails = {}
            )
        }

        composeTestRule.onNodeWithText(DataError.Network.UNKNOWN.asUiText()).assertExists().assertIsDisplayed()
    }
}

private fun ComposeTestRule.scratchImage(
    stepX: Float = 50f  // Horizontal distance between successive vertical dredges
) {
    val node = onNodeWithTag(IMAGE_SCRATCH).fetchSemanticsNode()
    val bounds = node.boundsInRoot

    val startX = bounds.left
    val endX = bounds.right
    val topY = bounds.top
    val bottomY = bounds.bottom

    var currentX = startX

    while (currentX < endX) {
        onNodeWithTag(IMAGE_SCRATCH).performTouchInput {
            val start = Offset(currentX, topY)
            val end = Offset(currentX, bottomY)

            down(start)
            moveTo(end)
            up()
        }

        currentX += stepX
    }
}