package com.adamdawi.popcornpicks.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adamdawi.popcornpicks.core.domain.OnBoardingManager
import com.adamdawi.popcornpicks.feature.genres_choose.presentation.GenresScreen
import com.adamdawi.popcornpicks.feature.movie_choose.presentation.MovieChooseScreen
import com.adamdawi.popcornpicks.feature.movie_details.presentation.MovieDetailsScreen
import com.adamdawi.popcornpicks.feature.recommendations.presentation.RecommendationsScreen
import com.adamdawi.popcornpicks.feature.user_profile.presentation.ProfileScreen
import org.koin.compose.koinInject

@Composable
fun Navigation(
    onboardingManager: OnBoardingManager = koinInject()
) {
    val navController = rememberNavController()

    var startDestination = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val isOnboardingCompleted = onboardingManager.isOnboardingCompleted()
        startDestination.value = if (isOnboardingCompleted) {
            Screen.Recommendations.route
        } else {
            Screen.Genres.route
        }
    }
    if(startDestination.value != null){
        NavHostWithSlideTransitions(
            navController = navController,
            startDestination = startDestination.value!!
        ) {
            composable(Screen.Genres.route) {
                GenresScreen(
                    onContinueClick = {
                        navController.navigate(Screen.MovieChoose.route)
                    }
                )
            }
            composable(Screen.MovieChoose.route) {
                MovieChooseScreen(
                    onFinishClick = {
                        navController.navigate(Screen.Recommendations.route)
                    }
                )
            }
            composable(Screen.Recommendations.route) {
                RecommendationsScreen(
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile.route)
                    },
                    onNavigateToMovieDetails = {
                        navController.navigate(Screen.MovieDetails.route)
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.MovieDetails.route) {
                MovieDetailsScreen()
            }

        }
    }
}