package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class ProfileImageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileImage_isProperlyDisplayed() {
        composeTestRule.setContent {
            ProfileImage(
                onClick = {}
            )
        }
        composeTestRule.onNodeWithTag(PROFILE_IMAGE).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("profile image").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("edit image").assertIsDisplayed()
    }

    @Test
    fun profileImage_performsClickAction() {
        var isClicked = false
        composeTestRule.setContent {
            ProfileImage(
                onClick = {
                    isClicked = true
                }
            )
        }
        composeTestRule.onNodeWithTag(PROFILE_IMAGE).performClick()
        assertTrue(isClicked)
    }

}