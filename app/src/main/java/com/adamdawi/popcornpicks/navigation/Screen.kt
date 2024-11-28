package com.adamdawi.popcornpicks.navigation

sealed class Screen(val route: String) {
    data object Genres: Screen("genres_screen")
    data object MovieChoose: Screen("movie_choose_screen")
}