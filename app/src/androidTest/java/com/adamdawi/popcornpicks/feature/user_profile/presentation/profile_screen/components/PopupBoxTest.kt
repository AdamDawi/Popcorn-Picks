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
    fun popupDialog_showPopupIsTrue_popupDialogIsDisplayed() {
        composeTestRule.setContent {
            PopupDialog(
                showPopup = true,
                onDismiss = {}
            ){}
        }

        composeTestRule.onNodeWithTag(POPUP_BOX).assertExists().assertIsDisplayed()
    }

    @Test
    fun popupDialog_showPopupIsFalse_popupDialogIsNotDisplayed() {
        composeTestRule.setContent {
            PopupDialog(
                showPopup = false,
                onDismiss = {}
            ){}
        }

        composeTestRule.onNodeWithTag(POPUP_BOX).assertIsNotDisplayed().assertDoesNotExist()
    }


    @Test
    fun popupDialog_popupDialogContentIsNotEmpty_popupDialogContentDisplayedCorrectly() {
        composeTestRule.setContent {
            PopupDialog(
                showPopup = true,
                onDismiss = {}
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