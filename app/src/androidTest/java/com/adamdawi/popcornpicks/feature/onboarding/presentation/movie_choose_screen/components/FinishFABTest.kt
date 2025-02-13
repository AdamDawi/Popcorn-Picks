package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.FINISH_FAB
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class FinishFABTest{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun finishFAB_isDisplayed(){
        composeTestRule.setContent {
            FinishFAB(
                isTextExpanded = true,
                onFinishClick = {},
                enabled = true
            )
        }
        composeTestRule.onNodeWithTag(FINISH_FAB).assertExists().assertIsDisplayed()
    }

    @Test
    fun finishFAB_enabled_performsClickAction(){
        var clicked = false
        composeTestRule.setContent {
            FinishFAB(
                isTextExpanded = true,
                onFinishClick = { clicked = true },
                enabled = true
            )
        }
        composeTestRule.onNodeWithTag(FINISH_FAB).performClick()
        assertTrue(clicked)
    }

    @Test
    fun finishFAB_disabled_doesNotPerformClickAction(){
        var clicked = false
        composeTestRule.setContent {
            FinishFAB(
                isTextExpanded = true,
                onFinishClick = { clicked = true },
                enabled = false
            )
        }
        composeTestRule.onNodeWithTag(FINISH_FAB).performClick()
        assertFalse(clicked)
    }

    @Test
    fun finishFAB_expanded_textIsDisplayed(){
        composeTestRule.setContent {
            FinishFAB(
                isTextExpanded = true,
                onFinishClick = {},
                enabled = false
            )
        }
        composeTestRule.onNodeWithText("Finish", useUnmergedTree = true).assertExists().assertIsDisplayed()
    }

    @Test
    fun finishFAB_notExpanded_textIsNotDisplayed(){
        composeTestRule.setContent {
            FinishFAB(
                isTextExpanded = false,
                onFinishClick = {},
                enabled = false
            )
        }
        composeTestRule.onNodeWithText("Finish", useUnmergedTree = true).assertDoesNotExist()
    }
}