package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

sealed interface ProfileAction {
    data object OnBackClicked: ProfileAction
}