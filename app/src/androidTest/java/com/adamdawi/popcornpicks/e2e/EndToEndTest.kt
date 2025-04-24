@file:OptIn(ExperimentalTestApi::class)

package com.adamdawi.popcornpicks.e2e

import android.content.SharedPreferences
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.app.navigation.Navigation
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.CIRCLE_ICON_BUTTON
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.FINISH_FAB
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.GENRE_CHIP
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.IMAGE_SCRATCH
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LIKED_MOVIES_LIST
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LIKED_MOVIE_ITEM
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_ITEM
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.POSTER_IMAGE
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE_NOT_SELECTED
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE_SELECTED
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.feature.movie_details.data.di.movieDetailsDataModule
import com.adamdawi.popcornpicks.feature.movie_details.presentation.di.movieDetailsViewModelModule
import com.adamdawi.popcornpicks.feature.onboarding.data.di.onboardingDataModule
import com.adamdawi.popcornpicks.feature.onboarding.presentation.di.onboardingViewModelModule
import com.adamdawi.popcornpicks.feature.recommendations.data.di.recommendationsDataModule
import com.adamdawi.popcornpicks.feature.recommendations.presentation.di.recommendationsViewModelModule
import com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.di.likedMoviesViewModelModule
import com.adamdawi.popcornpicks.feature.user_profile.data.di.profileDataModule
import com.adamdawi.popcornpicks.feature.user_profile.presentation.di.profileViewModelModule
import com.adamdawi.popcornpicks.utils.KoinTestRule
import com.adamdawi.popcornpicks.utils.scratchImage
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.java.KoinJavaComponent.inject

class EndToEndTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(
            testCoreDataModule,
            onboardingDataModule,
            onboardingViewModelModule,
            recommendationsDataModule,
            recommendationsViewModelModule,
            movieDetailsDataModule,
            movieDetailsViewModelModule,
            profileDataModule,
            profileViewModelModule,
            likedMoviesViewModelModule
        )
    )
    private val sharedPreferences by inject<SharedPreferences>(clazz = SharedPreferences::class.java)

    @Before
    fun setup(){
        sharedPreferences.edit().clear().commit()
    }

    @Test
    fun recommendationsScreen_scratchingMovieWorks() {
        composeTestRule.setContent {
            PopcornPicksTheme {
                Navigation()
            }
        }
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GENRE_CHIP))
        composeTestRule.onAllNodesWithTag(GENRE_CHIP)[0].performClick()
        composeTestRule.onAllNodesWithTag(GENRE_CHIP)[1].performClick()

        composeTestRule.onNodeWithText("Continue").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(MOVIE_ITEM))

        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[0].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[1].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[2].performClick()

        composeTestRule.onNodeWithTag(FINISH_FAB).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(IMAGE_SCRATCH))

        composeTestRule.scratchImage()

        // assert that buttons are enabled
        val buttons = composeTestRule.onAllNodesWithTag(CIRCLE_ICON_BUTTON)
        buttons.assertCountEquals(3)
        for (i in 0..<3) {
            val button = buttons[i]
            button.assertIsDisplayed().assertIsEnabled()
        }
    }

    @Test
    fun profileScreen_hasCorrectInformation() {
        composeTestRule.setContent {
            PopcornPicksTheme {
                Navigation()
            }
        }
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GENRE_CHIP))
        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.onNodeWithText("Adventure").performClick()

        composeTestRule.onNodeWithText("Continue").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(MOVIE_ITEM))

        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[0].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[1].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[2].performClick()

        composeTestRule.onNodeWithTag(FINISH_FAB).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(IMAGE_SCRATCH))

        composeTestRule.onNodeWithContentDescription("Profile Icon").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PROFILE_IMAGE))

        composeTestRule.onNodeWithText("Profile").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("YOU").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Action").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Adventure").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertExists().assertIsDisplayed()
    }

    @Test
    fun detailsScreen_hasCorrectMovieInformationWithRecommendationsScreen() {
        composeTestRule.setContent {
            PopcornPicksTheme {
                Navigation()
            }
        }
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GENRE_CHIP))
        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.onNodeWithText("Adventure").performClick()

        composeTestRule.onNodeWithText("Continue").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(MOVIE_ITEM))

        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[0].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[1].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[2].performClick()

        composeTestRule.onNodeWithTag(FINISH_FAB).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(IMAGE_SCRATCH))

        composeTestRule.scratchImage()

        composeTestRule.onNodeWithText("Fake Movie 1").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("2023 · Animation/Adventure · 8.5/10").assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Info").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(POSTER_IMAGE))

        composeTestRule.onNodeWithText("Fake Movie 1").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Overview of Fake Movie 1").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("8.50/10").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("2023-01").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Animation").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("2h 0m").assertExists().assertIsDisplayed()
    }

    @Test
    fun profileScreen_likedMoviesCounterUpdatedAfterAddMovieToLiked() {
        composeTestRule.setContent {
            PopcornPicksTheme {
                Navigation()
            }
        }
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GENRE_CHIP))
        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.onNodeWithText("Adventure").performClick()

        composeTestRule.onNodeWithText("Continue").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(MOVIE_ITEM))

        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[0].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[1].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[2].performClick()

        composeTestRule.onNodeWithTag(FINISH_FAB).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(IMAGE_SCRATCH))

        composeTestRule.onNodeWithContentDescription("Profile Icon").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PROFILE_IMAGE))

        composeTestRule.onNodeWithText("3").assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Arrow back").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(IMAGE_SCRATCH))

        composeTestRule.scratchImage()
        composeTestRule.onNodeWithContentDescription("Heart").performClick()

        composeTestRule.onNodeWithContentDescription("Profile Icon").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PROFILE_IMAGE))

        composeTestRule.onNodeWithText("4").assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMoviesScreen_likedMoviesListUpdatedAfterAddMovieToLiked() {
        composeTestRule.setContent {
            PopcornPicksTheme {
                Navigation()
            }
        }
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GENRE_CHIP))
        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.onNodeWithText("Adventure").performClick()

        composeTestRule.onNodeWithText("Continue").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(MOVIE_ITEM))

        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[0].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[1].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[2].performClick()

        composeTestRule.onNodeWithTag(FINISH_FAB).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(IMAGE_SCRATCH))

        composeTestRule.onNodeWithContentDescription("Profile Icon").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PROFILE_IMAGE))

        composeTestRule.onNodeWithText("Movies").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(LIKED_MOVIE_ITEM))

        composeTestRule.onNodeWithTag(LIKED_MOVIES_LIST)
            .performScrollToNode(hasText("Genre Movie 3 (2021)"))
        composeTestRule.onNodeWithText("Genre Movie 3 (2021)").assertExists().assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Arrow back").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PROFILE_IMAGE))
        composeTestRule.onNodeWithContentDescription("Arrow back").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(IMAGE_SCRATCH))

        composeTestRule.scratchImage()
        composeTestRule.onNodeWithContentDescription("Heart").performClick()

        composeTestRule.onNodeWithContentDescription("Profile Icon").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PROFILE_IMAGE))

        composeTestRule.onNodeWithText("Movies").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(LIKED_MOVIE_ITEM))

        composeTestRule.onNodeWithTag(LIKED_MOVIES_LIST)
            .performScrollToNode(hasText("Fake Movie 1 (2023)"))
        composeTestRule.onNodeWithText("Fake Movie 1 (2023)").assertExists().assertIsDisplayed()
    }

    @Test
    fun profileScreen_savingNewProfileImageRemainsAfterExitAndReopen() {
        composeTestRule.setContent {
            PopcornPicksTheme {
                Navigation()
            }
        }
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(GENRE_CHIP))
        composeTestRule.onNodeWithText("Action").performClick()
        composeTestRule.onNodeWithText("Adventure").performClick()

        composeTestRule.onNodeWithText("Continue").performClick()
        composeTestRule.waitUntilAtLeastOneExists(hasTestTag(MOVIE_ITEM))

        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[0].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[1].performClick()
        composeTestRule.onAllNodesWithTag(MOVIE_ITEM)[2].performClick()

        composeTestRule.onNodeWithTag(FINISH_FAB).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(IMAGE_SCRATCH))

        composeTestRule.onNodeWithContentDescription("Profile Icon").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PROFILE_IMAGE))

        composeTestRule.onNodeWithTag(PROFILE_IMAGE).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("Save"))

        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + R.drawable.sunglasses_guard.toString() + PROFILE_IMAGE_NOT_SELECTED).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasContentDescription(PROFILE_IMAGE + R.drawable.sunglasses_guard.toString()))
        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + R.drawable.sunglasses_guard.toString() + PROFILE_IMAGE_SELECTED).assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PROFILE_IMAGE))

        composeTestRule.onNodeWithContentDescription("Arrow back").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(IMAGE_SCRATCH))

        composeTestRule.onNodeWithContentDescription("Profile Icon").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(PROFILE_IMAGE))

        composeTestRule.onNodeWithTag(PROFILE_IMAGE).performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("Save"))
        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + R.drawable.sunglasses_guard.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + R.drawable.sunglasses_guard.toString() + PROFILE_IMAGE_SELECTED).assertIsDisplayed()
    }
}