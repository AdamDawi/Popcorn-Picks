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
        const val LOADING_SCREEN = "loading_screen"
        const val FINISH_FAB = "finish_fab"
        const val FINISH_FAB_ENABLED = "Enabled finish FAB"
        const val FINISH_FAB_DISABLED = "Disabled finish FAB"
        const val MOVIE_ITEM = "movie_item"
        const val MOVIE_ITEM_SELECTED = "Selected movie item"
        const val MOVIE_ITEM_NOT_SELECTED = "Not selected movie item"
        const val REGULAR_IMAGE = "regular_image"
        const val IMAGE_WITH_ANIMATED_BORDER = "image_with_animated_border"
        const val CARD_WITH_ANIMATED_BORDER = "card_with_animated_border"
        const val PROFILE_IMAGE = "profile_image_box"
        const val TOP_APP_BAR_ACTIONS = "top_app_bar_actions"
        const val TOP_APP_BAR_NAVIGATION_ICON = "top_app_bar_navigation_icon"
        const val ICON_LABEL_CHIP = "icon_label_chip"
        const val SMART_FLOW_ROW = "smart_flow_row"
        const val EDIT_ICON = "edit_icon"
        const val POPUP_BOX = "popup_box"
        const val POPUP_BOX_CONTENT = "popup_box_content"
        const val TEXT_CHIP = "text_chip"
        const val LIKED_MOVIE_ITEM = "liked_movie_item"
        const val PROFILE_IMAGE_SELECTED = "Selected profile image"
        const val PROFILE_IMAGE_NOT_SELECTED = "Not selected profile image"
        const val LAZY_MOVIES_GRID = "lazy_movies_grid"
        const val IMAGE_SCRATCH = "image_scratch"
        const val MOVIE_DETAILS_VISIBLE = "movie details visible"
        const val MOVIE_DETAILS_NOT_VISIBLE = "movie details not visible"
        const val CIRCLE_ICON_BUTTON = "circle_icon_button"
    }
}