package com.adamdawi.popcornpicks.core.domain.util

object Constants {
    object Network{
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/original"
    }
    object Local{
        const val SHARED_PREFERENCES_NAME = "popcorn_picks_preferences"
        val DEFAULT_GENRES_IDS = listOf("28", "12", "878")
    }

    object Database {
        const val DB_NAME = "popcorn_picks_db"
        const val LIKED_MOVIES_TABLE = "liked_movies_table"
        const val RECOMMENDED_MOVIES_TABLE = "recommended_movies_table"
    }

    object NavArguments {
        const val MOVIE_ID = "movie_id"
    }

    object SavedStateHandleArguments{
        const val IS_MOVIE_SCRATCHED = "is_movie_scratched"
    }

    object Tests{
        const val GENRE_SELECTED = "Selected genre"
        const val GENRE_NOT_SELECTED = "Not selected genre"
        const val GENRE_CHIP = "genre_chip"
        const val ERROR_SCREEN = "error_screen"
        const val FINISH_FAB = "finish_fab"
        const val MOVIE_ITEM = "movie_item"
        const val REGULAR_IMAGE = "regular_image"
        const val IMAGE_WITH_ANIMATED_BORDER = "image_with_animated_border"
        const val CARD_WITH_ANIMATED_BORDER = "card_with_animated_border"
        const val PROFILE_IMAGE = "profile_image_box"
        const val TOP_APP_BAR_ACTIONS = "top_app_bar_actions"
        const val TOP_APP_BAR_NAVIGATION_ICON = "top_app_bar_navigation_icon"
    }
}