package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.KoinTestRule
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.ICON_LABEL_CHIP
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LOADING_SCREEN
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE_EDIT_CONTENT
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.theme.ImageRed
import com.adamdawi.popcornpicks.feature.user_profile.domain.ProfileImageStyle
import com.adamdawi.popcornpicks.feature.user_profile.domain.local.UserProfilePreferences
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

class ProfileScreenTest {

    @get: Rule
    val composeTestRule = createComposeRule()
    private val genreList = listOf(
        Genre(id = 36, name = "History"),
        Genre(id = 27, name = "Horror"),
        Genre(id = 10402, name = "Music")
    )

    private val genresPreferences: GenresPreferences = mockk(relaxed = true)
    private val userProfilePreferences: UserProfilePreferences = mockk(relaxed = true)
    private val likedMoviesDbRepository: LikedMoviesDbRepository = mockk(relaxed = true)

    private val instrumentedTestModule = module {
        factory { genresPreferences }
        factory { userProfilePreferences }
        factory { likedMoviesDbRepository }
        factory { Dispatchers.IO }
        viewModel { ProfileViewModel(get(), get(), get(), get()) }
    }

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(instrumentedTestModule)
    )

    private val viewModel by inject<ProfileViewModel>(clazz = ProfileViewModel::class.java)

    @Before
    fun setup() {
        every { genresPreferences.getGenres() } answers { emptyList() }
        coEvery { likedMoviesDbRepository.getLikedMoviesCount() } answers { Result.Success(null) }
        every { userProfilePreferences.getProfileImageStyle() } answers {
            ProfileImageStyle(
                imageId = R.drawable.happy_guard,
                ImageRed.toArgb()
            )
        }
        every { userProfilePreferences.saveProfileImageStyle(any()) } answers {}
    }

    @Test
    fun profileScreen_emptyGenresList_correctTextIsDisplayed() {
        composeTestRule.setContent {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToLikedMoviesScreen = {}
            )
        }

        composeTestRule.onNodeWithText("No genres available").assertExists().assertIsDisplayed()
    }
    @Test
    fun profileScreen_notEmptyGenresList_correctGenresAreDisplayed() {
        val genreIdsList = genreList.map { it.id.toString() }
        every { genresPreferences.getGenres() } answers { genreIdsList }
        composeTestRule.setContent {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToLikedMoviesScreen = {}
            )
        }

        genreList.forEach { genre ->
            composeTestRule.onNodeWithText(genre.name).assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun profileScreen_notEmptyGenresList_correctNumberOfGenresAreDisplayed() {
        val genreIdsList = genreList.map { it.id.toString() }
        every { genresPreferences.getGenres() } answers { genreIdsList }
        composeTestRule.setContent {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToLikedMoviesScreen = {}
            )
        }

        composeTestRule.onAllNodesWithTag(ICON_LABEL_CHIP).assertCountEquals(genreIdsList.size)
    }

    @Test
    fun profileScreen_likedMoviesCountIsNull_correctTextIsDisplayed() {
        composeTestRule.setContent {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToLikedMoviesScreen = {}
            )
        }

        composeTestRule.onNodeWithText("No movies information available").assertExists().assertIsDisplayed()
    }

    @Test
    fun profileScreen_likedMoviesCountIsNotNull_correctCountIsDisplayed() {
        coEvery { likedMoviesDbRepository.getLikedMoviesCount() } answers { Result.Success(28) }
        composeTestRule.setContent {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToLikedMoviesScreen = {}
            )
        }

        composeTestRule.onNodeWithText("28").assertExists().assertIsDisplayed()
    }

    @Test
    fun profileScreen_isLoading_loadingScreenDisplayed() {
        coEvery { likedMoviesDbRepository.getLikedMoviesCount() } coAnswers {
            delay(500)
            Result.Error(DataError.Local.UNKNOWN)
        }

        composeTestRule.setContent {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToLikedMoviesScreen = {}
            )
        }

        composeTestRule.onNodeWithTag(LOADING_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun profileScreen_notShowPopupDialog_popupDialogIsNotDisplayed(){
        composeTestRule.setContent {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToLikedMoviesScreen = {}
            )
        }

        composeTestRule.onNodeWithTag(PROFILE_IMAGE_EDIT_CONTENT).assertIsNotDisplayed().assertDoesNotExist()
    }

    @Test
    fun profileScreen_showPopupDialog_popupDialogIsDisplayed(){
        composeTestRule.setContent {
            ProfileScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToLikedMoviesScreen = {}
            )
        }
        composeTestRule.onNodeWithTag(PROFILE_IMAGE).performClick()

        composeTestRule.onNodeWithTag(PROFILE_IMAGE_EDIT_CONTENT).assertExists().assertIsDisplayed()
    }
}