@file:OptIn(ExperimentalTestApi::class)

package com.adamdawi.popcornpicks.e2e

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.app.navigation.Navigation
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.CIRCLE_ICON_BUTTON
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.FINISH_FAB
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.GENRE_CHIP
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.IMAGE_SCRATCH
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_ITEM
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
import org.junit.Rule
import org.junit.Test

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

    @Test
    fun recommendationsScreen_scratchingMovieWorks() {
        composeTestRule.setContent {
            PopcornPicksTheme {
                Navigation()
            }
        }
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
}