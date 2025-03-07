package com.adamdawi.popcornpicks.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adamdawi.popcornpicks.core.domain.local.OnBoardingManager
import com.adamdawi.popcornpicks.core.domain.util.Constants.NavArguments.MOVIE_ID
import com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen.MovieDetailsScreen
import com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen.GenresScreen
import com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.MovieChooseScreen
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.RecommendationsScreen
import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.ProfileScreen
import org.koin.compose.koinInject

@Composable
fun Navigation(
    onboardingManager: OnBoardingManager = koinInject()
) {
    val navController = rememberNavController()

    NavHostWithSlideTransitions(
        navController = navController,
        startDestination = if (onboardingManager.isOnboardingCompleted()) Screen.Recommendations.route else Screen.Genres.route
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
                    navController.navigate(Screen.MovieDetails.passMovieId(movieId = it))
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composableWithSlideToLeftTransitions(
            route = Screen.MovieDetails.route,
            arguments = listOf(
                navArgument(MOVIE_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            MovieDetailsScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}