package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.domain.model.Genre

data class ProfileState(
    val genres: List<Genre> = dummyGenresList.take(4),
    val likedMoviesCount: Int = 0,
    val isLoading: Boolean = false
)