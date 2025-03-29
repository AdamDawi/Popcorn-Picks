package com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.core.KoinTestRule
import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.ERROR_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.GENRE_CHIP
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LOADING_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.onboarding.domain.remote.GenresRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class GenresScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository: GenresRepository = mockk(relaxed = true)
    private val genresPreferences: GenresPreferences = mockk(relaxed = true)

//    private val instrumentedTestModule = module {
//        factory { repository }
//        factory { genresPreferences }
//        viewModel { GenresViewModel(get(), get()) }
//    }
//
//    @get:Rule
//    val koinTestRule = KoinTestRule(
//        modules = listOf(instrumentedTestModule)
//    )
//
//    private val viewModel by inject<GenresViewModel>(clazz = GenresViewModel::class.java)

    lateinit var viewModel: GenresViewModel
    @Before
    fun setup() {
        viewModel = GenresViewModel(
            repository,
            genresPreferences
        )
        coEvery { repository.getGenres() } answers {
            Result.Success(dummyGenresList)
        }
    }

    @Test
    fun genresScreen_emptyGenresList_genreChipsNotDisplayed() {
        coEvery { repository.getGenres() } answers {
            Result.Success(emptyList())
        }

        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        composeTestRule.onAllNodesWithTag(GENRE_CHIP).assertCountEquals(0)
    }

    @Test
    fun genresScreen_emptyGenresList_errorScreenDisplayed() {
        coEvery { repository.getGenres() } answers {
            Result.Success(emptyList())
        }

        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        composeTestRule.onNodeWithTag(ERROR_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun genresScreen_emptyGenresList_correctErrorInErrorScreenDisplayed() {
        coEvery { repository.getGenres() } answers {
            Result.Success(emptyList())
        }

        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        composeTestRule.onNodeWithText("Something went wrong").assertExists().assertIsDisplayed()
    }

    @Test
    fun genresScreen_notEmptyGenresList_correctNumberOfGenreChipsDisplayed() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }
        composeTestRule.waitForIdle()
        val nodes = composeTestRule.onAllNodesWithTag(GENRE_CHIP)
        nodes.assertCountEquals(dummyGenresList.size)
        for (i in 0..<dummyGenresList.size) {
            val node = nodes[i]
            node.assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun genresScreen_notEmptyGenresList_correctTextInGenreChipsDisplayed() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }
        composeTestRule.waitForIdle()
        dummyGenresList.forEach { genre ->
            composeTestRule.onNodeWithText(genre.name).assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun genresScreen_zeroGenresSelected_continueButtonDisabled() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Continue").assertExists().assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun genresScreen_zeroGenresSelected_zeroGenresInSelectedGenresState() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }
        composeTestRule.waitForIdle()

        assertTrue(viewModel.state.value.selectedGenres.isEmpty())
    }

    @Test
    fun genresScreen_oneGenreSelected_continueButtonDisabled() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }
        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Continue").assertExists().assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun genresScreen_oneGenreSelected_oneGenreInSelectedGenresState() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }
        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.waitForIdle()

        assertTrue(viewModel.state.value.selectedGenres.size == 1)
    }

    @Test
    fun genresScreen_twoGenresSelected_continueButtonEnabled() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.onNodeWithText("Mystery").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Continue").assertExists().assertIsDisplayed().assertIsEnabled()
    }

    @Test
    fun genresScreen_twoGenresSelected_towGenresInSelectedGenresState() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }
        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.onNodeWithText("Mystery").performClick()
        composeTestRule.waitForIdle()

        assertTrue(viewModel.state.value.selectedGenres.size == 2)
    }

    @Test
    fun genresScreen_fiveGenresSelected_continueButtonEnabled() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.onNodeWithText("Mystery").performClick()
        composeTestRule.onNodeWithText("History").performClick()
        composeTestRule.onNodeWithText("Music").performClick()
        composeTestRule.onNodeWithText("Romance").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Continue").assertExists().assertIsDisplayed().assertIsEnabled()
    }

    @Test
    fun genresScreen_fiveGenresSelected_fiveGenresInSelectedGenresState() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.onNodeWithText("Mystery").performClick()
        composeTestRule.onNodeWithText("History").performClick()
        composeTestRule.onNodeWithText("Music").performClick()
        composeTestRule.onNodeWithText("Romance").performClick()

        composeTestRule.waitForIdle()

        assertTrue(viewModel.state.value.selectedGenres.size == 5)
    }

    @Test
    fun genresScreen_fiveGenresSelected_selectedGenresStateUpdatedWithCorrectGenres() {
        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        val listOfGenres = listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 12, name = "Adventure"),
            Genre(id = 16, name = "Animation"),
            Genre(id = 35, name = "Comedy"),
            Genre(id = 80, name = "Crime")
        )
        listOfGenres.forEach { genre ->
            composeTestRule.onNodeWithText(genre.name).performClick()

        }
        composeTestRule.waitForIdle()

        assertThat(viewModel.state.value.selectedGenres).isEqualTo(listOfGenres)
    }

    @Test
    fun genresScreen_isLoading_loadingScreenDisplayed() {
        coEvery { repository.getGenres() } coAnswers {
            delay(500)
            Result.Error(DataError.Network.UNKNOWN)
        }

        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        composeTestRule.onNodeWithTag(LOADING_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun genresScreen_error_errorScreenDisplayed() {
        coEvery { repository.getGenres() } answers { Result.Error(DataError.Network.UNKNOWN) }

        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        composeTestRule.onNodeWithTag(ERROR_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun genresScreen_error_correctErrorInErrorScreenDisplayed() {
        coEvery { repository.getGenres() } answers { Result.Error(DataError.Network.UNKNOWN) }

        composeTestRule.setContent {
            GenresScreen(
                viewModel = viewModel,
                onContinueClick = {}
            )
        }

        composeTestRule.onNodeWithText(DataError.Network.UNKNOWN.asUiText()).assertExists().assertIsDisplayed()
    }
}