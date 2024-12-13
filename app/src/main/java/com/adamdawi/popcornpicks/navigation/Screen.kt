package com.adamdawi.popcornpicks.navigation

sealed class Screen(val route: String) {
    data object Genres: Screen("genres_screen")
    data object MovieChoose: Screen("movie_choose_screen")
    data object Recommendations: Screen("recommendations_screen")
    data object Profile: Screen("profile_screen")
    data object MovieDetails: Screen("movie_details_screen")
}