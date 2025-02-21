package com.adamdawi.popcornpicks.app.navigation

import com.adamdawi.popcornpicks.core.domain.util.Constants.NavArguments.MOVIE_ID

sealed class Screen(val route: String) {
    data object Genres: Screen("genres_screen")
    data object MovieChoose: Screen("movie_choose_screen")
    data object Recommendations: Screen("recommendations_screen")
    data object Profile: Screen("profile_screen")
    data object MovieDetails: Screen("movie_details_screen/{$MOVIE_ID}"){
        fun passMovieId(movieId: String) = "movie_details_screen/$movieId"
    }
}