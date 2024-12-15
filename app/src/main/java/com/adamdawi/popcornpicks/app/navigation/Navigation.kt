package com.adamdawi.popcornpicks.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adamdawi.popcornpicks.core.domain.OnBoardingManager
import com.adamdawi.popcornpicks.core.presentation.ui.LoadingScreen
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
            composable(
                route = Screen.Recommendations.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.MovieDetails.route -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            tween(400)
                        )
                        else -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Companion.Start,
                            tween(400)
                        )
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        Screen.MovieDetails.route -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            tween(400)
                        )
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Companion.Start,
                            tween(400)
                        )
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        Screen.MovieDetails.route -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            tween(400)
                        )
                        else -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Companion.End,
                            tween(400)
                        )
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        Screen.MovieDetails.route -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            tween(400)
                        )
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Companion.End,
                            tween(400)
                        )
                    }
                }
            ) {
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
            composableWithSlideToLeftTransitions(
                route = Screen.MovieDetails.route
            ) {
                MovieDetailsScreen(
                    onNavigateBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }else{
        LoadingScreen()
    }
}