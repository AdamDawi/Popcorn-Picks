package com.adamdawi.popcornpicks.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.app.navigation.Navigation
import com.adamdawi.popcornpicks.core.KoinTestRule
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
import org.junit.Rule
import org.junit.Test

class EndToEndTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(
            testDataModule,
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


//    @Test
//    fun movieDetailsScreen_success_correctTitleIsDisplayed() {
//        composeTestRule.setContent {
//            PopcornPicksTheme {
//                Navigation()
//            }
//        }
//        composeTestRule.onNodeWithText("Continue").performClick()
//        composeTestRule.waitUntil {
//            false
//        }
//        composeTestRule.onNodeWithText("Spider-Man: No Way Home").assertExists().assertIsDisplayed()
//    }
}