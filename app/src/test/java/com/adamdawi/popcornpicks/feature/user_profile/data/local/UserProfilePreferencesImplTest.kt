package com.adamdawi.popcornpicks.feature.user_profile.data.local

import android.content.SharedPreferences
import androidx.compose.ui.graphics.toArgb
import com.adamdawi.popcornpicks.core.data.dummy.profileImages
import com.adamdawi.popcornpicks.core.presentation.theme.ImageRed
import com.adamdawi.popcornpicks.feature.user_profile.domain.ProfileImageStyle
import com.adamdawi.popcornpicks.feature.user_profile.domain.local.UserProfilePreferences
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class UserProfilePreferencesImplTest {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sut: UserProfilePreferences

    private val profileImageKey = "profile_image_key"
    private val profileImageBgKey = "profile_image_bg_key"

    @Before
    fun setUp() {
        sharedPreferences = mockk()
        editor = mockk()
        sut = UserProfilePreferencesImpl(sharedPreferences)

        // Default mock behavior for editing SharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { editor.putInt(any(), any()) } returns editor
        every { editor.apply() } just Runs
    }

    // SAVE PROFILE IMAGE STYLE
    @Test
    fun saveProfileImageStyle_correctProfileImageIdSavedInPreferences(){
        // Arrange
        val profileImageStyle = ProfileImageStyle(312, ImageRed.toArgb())

        // Act
        sut.saveProfileImageStyle(profileImageStyle)

        // Assert
        verify(exactly = 1) {
            editor.putInt(profileImageKey, 312)
        }
    }

    @Test
    fun saveProfileImageStyle_correctProfileImageBgSavedInPreferences(){
        // Arrange
        val profileImageStyle = ProfileImageStyle(312, ImageRed.toArgb())

        // Act
        sut.saveProfileImageStyle(profileImageStyle)

        // Assert
        verify(exactly = 1) {
            editor.putInt(profileImageBgKey, ImageRed.toArgb())
        }
    }

    @Test
    fun saveProfileImageStyle_sharedPreferencesEditAndApplyInvokedTwice(){
        // Arrange
        val profileImageStyle = ProfileImageStyle(312, ImageRed.toArgb())

        // Act
        sut.saveProfileImageStyle(profileImageStyle)

        // Assert
        verify(exactly = 2) {
            sharedPreferences.edit()
            editor.apply()
        }
    }

    // GET PROFILE IMAGE STYLE
    @Test
    fun getProfileImageStyle_profileImageIdSavedInSharedPreferences_correctProfileImageIdReturned(){
        // Arrange
        every { sharedPreferences.getInt(profileImageKey, any()) } returns 312
        every { sharedPreferences.getInt(profileImageBgKey, any()) } returns 0

        // Act
        val result = sut.getProfileImageStyle()

        // Assert
        assertThat(result.imageId).isEqualTo(312)
    }

    @Test
    fun getProfileImageStyle_profileImageIdNotSavedInSharedPreferences_defaultProfileImageIdReturned(){
        // Arrange
        every { sharedPreferences.getInt(profileImageKey, any()) } answers { secondArg() }
        every { sharedPreferences.getInt(profileImageBgKey, any()) } returns 0

        // Act
        val result = sut.getProfileImageStyle()

        // Assert
        assertThat(result.imageId).isEqualTo(profileImages[0])
    }

    @Test
    fun getProfileImageStyle_profileImageBgSavedInSharedPreferences_correctProfileImageBgReturned(){
        // Arrange
        every { sharedPreferences.getInt(profileImageKey, any()) } returns 0
        every { sharedPreferences.getInt(profileImageBgKey, any()) } returns ImageRed.toArgb()

        // Act
        val result = sut.getProfileImageStyle()

        // Assert
        assertThat(result.backgroundColor).isEqualTo(ImageRed.toArgb())
    }

    @Test
    fun getProfileImageStyle_profileImageBgNotSavedInSharedPreferences_defaultProfileImageBgReturned(){
        // Arrange
        every { sharedPreferences.getInt(profileImageKey, any()) } returns 0
        every { sharedPreferences.getInt(profileImageBgKey, any()) } answers { secondArg() }

        // Act
        val result = sut.getProfileImageStyle()

        // Assert
        assertThat(result.backgroundColor).isEqualTo(ImageRed.toArgb())
    }

    @Test
    fun getProfileImageStyle_correctDefaultProfileImageIdPassedInSharedPreferences(){
        // Arrange
        every { sharedPreferences.getInt(profileImageKey, any()) } returns 0
        every { sharedPreferences.getInt(profileImageBgKey, any()) } returns 0

        // Act
        sut.getProfileImageStyle()

        // Assert
        verify(exactly = 1){
            sharedPreferences.getInt(profileImageKey, profileImages[0])
        }
    }

    @Test
    fun getProfileImageStyle_correctDefaultProfileImageBgPassedInSharedPreferences(){
        // Arrange
        every { sharedPreferences.getInt(profileImageKey, any()) } returns 0
        every { sharedPreferences.getInt(profileImageBgKey, any()) } returns 0

        // Act
        sut.getProfileImageStyle()

        // Assert
        verify(exactly = 1){
            sharedPreferences.getInt(profileImageBgKey, ImageRed.toArgb())
        }
    }
}