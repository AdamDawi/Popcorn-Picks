package com.adamdawi.popcornpicks.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adamdawi.popcornpicks.feature.genres_choose.presentation.GenresScreen
import com.adamdawi.popcornpicks.feature.movie_choose.presentation.MovieChooseScreen
import com.adamdawi.popcornpicks.feature.movie_details.presentation.MovieDetailsScreen
import com.adamdawi.popcornpicks.feature.recommendations.presentation.RecommendationsScreen
import com.adamdawi.popcornpicks.feature.user_profile.presentation.ProfileScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHostWithSlideTransitions(
        navController = navController,
        startDestination = Screen.Genres.route
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