package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.POPUP_BOX
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.POPUP_BOX_CONTENT
import org.junit.Rule
import org.junit.Test

class PopupBoxTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun popupBox_showPopupIsTrue_popupBoxIsDisplayed() {
        composeTestRule.setContent {
            PopupBox(
                showPopup = true
            ){}
        }

        composeTestRule.onNodeWithTag(POPUP_BOX).assertExists().assertIsDisplayed()
    }

    @Test
    fun popupBox_showPopupIsFalse_popupBoxIsNotDisplayed() {
        composeTestRule.setContent {
            PopupBox(
                showPopup = false
            ){}
        }

        composeTestRule.onNodeWithTag(POPUP_BOX).assertIsNotDisplayed().assertDoesNotExist()
    }


    @Test
    fun popupBox_popupBoxContentIsNotEmpty_popupBoxContentDisplayedCorrectly() {
        composeTestRule.setContent {
            PopupBox(
                showPopup = true
            ){
                Text(
                    modifier = Modifier.testTag(POPUP_BOX_CONTENT),
                    text = "d"
                )
            }
        }

        composeTestRule.onNodeWithTag(POPUP_BOX_CONTENT).assertExists().assertIsDisplayed()
    }
}