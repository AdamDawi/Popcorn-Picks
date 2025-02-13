package com.adamdawi.popcornpicks.core.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.ERROR_SCREEN
import org.junit.Rule
import org.junit.Test

class ErrorScreenTest{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorScreen_isDisplayed() {
        composeTestRule.setContent {
            ErrorScreen()
        }
        composeTestRule.onNodeWithTag(ERROR_SCREEN).assertExists().assertIsDisplayed()
    }

    @Test
    fun errorScreen_messageNull_displaysDefaultMessage() {
        composeTestRule.setContent {
            ErrorScreen(
                message = null
            )
        }
        composeTestRule.onNodeWithText("Something went wrong").assertExists().assertIsDisplayed()
    }

    @Test
    fun errorScreen_messageNotNull_displaysCorrectMessage() {
        composeTestRule.setContent {
            ErrorScreen(
                message = "Internet connection lost"
            )
        }
        composeTestRule.onNodeWithText("Internet connection lost").assertExists().assertIsDisplayed()
    }
}