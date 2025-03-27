package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.TEXT_CHIP
import org.junit.Rule
import org.junit.Test

class TextChipTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textChip_visibility_displayed(){
        composeTestRule.setContent {
            TextChip(
                text = ""
            )
        }
        composeTestRule.onNodeWithTag(TEXT_CHIP).assertExists().assertIsDisplayed()
    }

    @Test
    fun textChip_notEmptyText_correctTextDisplayed(){
        composeTestRule.setContent {
            TextChip(
                text = "2023"
            )
        }
        composeTestRule.onNodeWithText("2023").assertExists().assertIsDisplayed()
    }

    @Test
    fun textChip_emptyText_emptyTextNotDisplayed(){
        composeTestRule.setContent {
            TextChip(
                text = ""
            )
        }
        composeTestRule.onNodeWithText("").assertExists().assertIsNotDisplayed()
    }
}