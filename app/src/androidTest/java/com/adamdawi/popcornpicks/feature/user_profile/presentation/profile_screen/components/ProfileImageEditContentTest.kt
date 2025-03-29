package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.core.data.dummy.profileImages
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE_SELECTED
import junit.framework.TestCase.assertTrue
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class ProfileImageEditContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun profileImageEditContent_zeroProfileImagesPassed_oneProfileImageDisplayed() {
        composeTestRule.setContent {
            ProfileImageEditContent(
                onSaveClick = {color, id ->},
                onCancelClick = {},
                userProfileImageId = profileImages[0],
                profileImages = emptyList()
            )
        }
        val nodes = composeTestRule.onAllNodesWithTag(PROFILE_IMAGE)
        nodes.assertCountEquals(1)

        nodes[0].assertIsDisplayed()
    }

    // Four profile images should be displayed:
    // three selectable images and one for showing the user's chosen profile image.
    @Test
    fun profileImageEditContent_threeProfileImagesPassed_fourProfileImagesDisplayed() {
        composeTestRule.setContent {
            ProfileImageEditContent(
                onSaveClick = {color, id ->},
                onCancelClick = {},
                userProfileImageId = profileImages[0],
                profileImages = profileImages.take(3)
            )
        }
        val nodes = composeTestRule.onAllNodesWithTag(PROFILE_IMAGE)
        nodes.assertCountEquals(4)

        for (index in 0..3) {
            val node = nodes[index]
            node.assertIsDisplayed()
        }
    }

    @Test
    fun profileImageEditContent_userProfileImageId_shouldDisplayTwoImagesOfUserProfileImageId() {
        composeTestRule.setContent {
            ProfileImageEditContent(
                onSaveClick = {color, id ->},
                onCancelClick = {},
                userProfileImageId = profileImages[2],
                profileImages = profileImages
            )
        }

        val nodes = composeTestRule.onAllNodesWithContentDescription(PROFILE_IMAGE + profileImages[2], substring = true)
        nodes.assertCountEquals(2)

        for (index in 0..1){
            val node = nodes[index]
            node.assertIsDisplayed()
        }
    }

    @Test
    fun profileImageEditContent_selectionOfProfileImageOneTime_shouldDisplayTwoImagesOfUserProfileImageId() {
        composeTestRule.setContent {
            ProfileImageEditContent(
                onSaveClick = {color, id ->},
                onCancelClick = {},
                userProfileImageId = profileImages[0],
                profileImages = profileImages
            )
        }

        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + profileImages[2], substring = true).performClick()

        val nodes = composeTestRule.onAllNodesWithContentDescription(PROFILE_IMAGE + profileImages[2], substring = true)
        nodes.assertCountEquals(2)

        for (index in 0..1){
            val node = nodes[index]
            node.assertIsDisplayed()
        }
    }

    @Test
    fun profileImageEditContent_userProfileImageId_shouldMarkOnlyThatImageAsSelectedOnInit() {
        composeTestRule.setContent {
            ProfileImageEditContent(
                onSaveClick = {color, id ->},
                onCancelClick = {},
                userProfileImageId = profileImages[2],
                profileImages = profileImages
            )
        }

        composeTestRule.onAllNodesWithTag(PROFILE_IMAGE).filter(hasContentDescription(PROFILE_IMAGE_SELECTED, substring = true)).assertCountEquals(1)
        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + profileImages[2] + PROFILE_IMAGE_SELECTED).assertExists().assertIsDisplayed()
    }

    @Test
    fun profileImageEditContent_selectionOfProfileImageOneTime_shouldMarkOnlyThatImageAsSelected() {
        composeTestRule.setContent {
            ProfileImageEditContent(
                onSaveClick = {color, id ->},
                onCancelClick = {},
                userProfileImageId = profileImages[0],
                profileImages = profileImages
            )
        }

        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + profileImages[1], substring = true).performClick()

        composeTestRule.onAllNodesWithTag(PROFILE_IMAGE).filter(hasContentDescription(PROFILE_IMAGE_SELECTED, substring = true)).assertCountEquals(1)
        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + profileImages[1] + PROFILE_IMAGE_SELECTED).assertExists().assertIsDisplayed()
    }

    @Test
    fun profileImageEditContent_selectionOfProfileImageTwoTimes_shouldMarkOnlyLastImageAsSelected() {
        composeTestRule.setContent {
            ProfileImageEditContent(
                onSaveClick = {color, id ->},
                onCancelClick = {},
                userProfileImageId = profileImages[0],
                profileImages = profileImages
            )
        }

        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + profileImages[1], substring = true).performClick()
        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + profileImages[3], substring = true).performClick()

        composeTestRule.onAllNodesWithTag(PROFILE_IMAGE).filter(hasContentDescription(PROFILE_IMAGE_SELECTED, substring = true)).assertCountEquals(1)
        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + profileImages[3] + PROFILE_IMAGE_SELECTED).assertExists().assertIsDisplayed()
    }

    @Test
    fun profileImageEditContent_cancel_performsClickAction() {
        var isCancelClicked = false
        composeTestRule.setContent {
            ProfileImageEditContent(
                onCancelClick = {
                    isCancelClicked = true
                },
                onSaveClick = {color, id ->},
                userProfileImageId = profileImages[0],
                profileImages = profileImages
            )
        }

        composeTestRule.onNodeWithText("Cancel").performClick()

        assertTrue(isCancelClicked)
    }

    @Test
    fun profileImageEditContent_save_performsClickAction() {
        var isSaveClicked = false
        composeTestRule.setContent {
            ProfileImageEditContent(
                onCancelClick = {},
                onSaveClick = {color, id -> isSaveClicked = true},
                userProfileImageId = profileImages[0],
                profileImages = profileImages
            )
        }

        composeTestRule.onNodeWithText("Save").performClick()

        assertTrue(isSaveClicked)
    }

    @Test
    fun profileImageEditContent_save_passCorrectParametersInCallback() {
        var colorRes = Color.Black
        var imageId = 0
        composeTestRule.setContent {
            ProfileImageEditContent(
                onCancelClick = {},
                onSaveClick = {color, id ->
                    colorRes = color
                    imageId = id
                },
                userProfileImageId = profileImages[0],
                profileImages = profileImages
            )
        }

        composeTestRule.onNodeWithText("Save").performClick()

        assertThat(colorRes).isEqualTo(Color.White)
        assertThat(imageId).isEqualTo(profileImages[0])
    }

    @Test
    fun profileImageEditContent_selectAnotherProfileImageThenSave_passCorrectParametersInCallback() {
        var colorRes = Color.Black
        var imageId = 0
        composeTestRule.setContent {
            ProfileImageEditContent(
                onCancelClick = {},
                onSaveClick = {color, id ->
                    colorRes = color
                    imageId = id
                },
                userProfileImageId = profileImages[0],
                profileImages = profileImages
            )
        }

        composeTestRule.onNodeWithContentDescription(PROFILE_IMAGE + profileImages[2], substring = true).performClick()
        composeTestRule.onNodeWithText("Save").performClick()

        assertThat(colorRes).isEqualTo(Color.White)
        assertThat(imageId).isEqualTo(profileImages[2])
    }
}