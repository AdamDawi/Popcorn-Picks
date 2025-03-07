package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import com.adamdawi.popcornpicks.core.domain.model.Genre

data class ProfileState(
    val genres: List<Genre> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)