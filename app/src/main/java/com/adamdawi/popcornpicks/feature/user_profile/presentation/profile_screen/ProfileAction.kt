package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

sealed interface ProfileAction {
    data object OnBackClicked: ProfileAction
    data object OnNavigateToLikedMovies: ProfileAction
    data class OnSaveProfileImageStyle(val profileImageId: Int, val profileImageBg: Int): ProfileAction
}