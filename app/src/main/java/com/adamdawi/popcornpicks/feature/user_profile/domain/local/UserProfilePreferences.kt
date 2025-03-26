package com.adamdawi.popcornpicks.feature.user_profile.domain.local

import com.adamdawi.popcornpicks.feature.user_profile.domain.ProfileImageStyle

interface UserProfilePreferences {
    fun saveProfileImageStyle(profileImageStyle: ProfileImageStyle)
    fun getProfileImageStyle(): ProfileImageStyle
}