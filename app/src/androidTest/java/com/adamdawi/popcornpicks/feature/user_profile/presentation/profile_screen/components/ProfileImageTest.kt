package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.EDIT_ICON
import com.adamdawi.popcornpicks.core.presentation.theme.ImageRed

class ProfileImageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileImage_isProperlyDisplayed() {
        composeTestRule.setContent {
            ProfileImage(
                onClick = {},
                backgroundColor = ImageRed,
                imageId = R.drawable.happy_guard
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
                },
                backgroundColor = ImageRed,
                imageId = R.drawable.happy_guard
            )
        }
        composeTestRule.onNodeWithTag(PROFILE_IMAGE).performClick()
        assertTrue(isClicked)
    }

    @Test
    fun profileImage_showEditIconIsTrue_editIconDisplayed() {
        composeTestRule.setContent {
            ProfileImage(
                onClick = {},
                backgroundColor = ImageRed,
                imageId = R.drawable.happy_guard,
                showEditIcon = true
            )
        }
        composeTestRule.onNodeWithTag(EDIT_ICON, useUnmergedTree = true).assertExists().assertIsDisplayed()
    }


    @Test
    fun profileImage_showEditIconIsFalse_editIconNotDisplayed() {
        composeTestRule.setContent {
            ProfileImage(
                onClick = {},
                backgroundColor = ImageRed,
                imageId = R.drawable.happy_guard,
                showEditIcon = false
            )
        }
        composeTestRule.onNodeWithTag(EDIT_ICON).assertIsNotDisplayed().assertDoesNotExist()
    }
}