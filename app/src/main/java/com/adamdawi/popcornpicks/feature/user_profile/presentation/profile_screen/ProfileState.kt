package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.feature.user_profile.domain.ProfileImageStyle

data class ProfileState(
    val genres: List<Genre> = emptyList(),
    val likedMoviesCount: Int? = null,
    val isLoading: Boolean = false,
    val profileImageStyle: ProfileImageStyle? = null
)