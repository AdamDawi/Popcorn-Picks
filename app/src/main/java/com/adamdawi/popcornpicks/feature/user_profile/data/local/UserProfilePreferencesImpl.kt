package com.adamdawi.popcornpicks.feature.user_profile.data.local

import android.content.SharedPreferences
import androidx.compose.ui.graphics.toArgb
import com.adamdawi.popcornpicks.core.data.dummy.profileImages
import com.adamdawi.popcornpicks.core.presentation.theme.ImageRed
import com.adamdawi.popcornpicks.feature.user_profile.domain.ProfileImageStyle
import com.adamdawi.popcornpicks.feature.user_profile.domain.local.UserProfilePreferences

class UserProfilePreferencesImpl(
    private val sharedPreferences: SharedPreferences
) : UserProfilePreferences {
    override fun saveProfileImageStyle(profileImageStyle: ProfileImageStyle) {
        sharedPreferences.edit()
            .putInt(PROFILE_IMAGE_KEY, profileImageStyle.imageId)
            .apply()

        sharedPreferences.edit()
            .putInt(PROFILE_IMAGE_BG_KEY, profileImageStyle.backgroundColor)
            .apply()
    }

    override fun getProfileImageStyle(): ProfileImageStyle {
        val profileImageId = sharedPreferences.getInt(PROFILE_IMAGE_KEY, profileImages[0])
        val profileImageBgColor = sharedPreferences.getInt(PROFILE_IMAGE_BG_KEY, ImageRed.toArgb())
        return ProfileImageStyle(
            profileImageId,
            profileImageBgColor
        )
    }

    companion object {
        private const val PROFILE_IMAGE_KEY = "profile_image_key"
        private const val PROFILE_IMAGE_BG_KEY = "profile_image_bg_key"
    }
}