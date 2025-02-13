package com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen.components

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.GENRE_CHIP
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.GENRE_NOT_SELECTED
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.GENRE_SELECTED
import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class GenreChipTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun genreChip_isDisplayed() {
        composeTestRule.setContent {
            GenreChip(
                genre = Genre(id = 1, name = "Action"),
                isSelected = true,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithTag(GENRE_CHIP).assertExists().assertIsDisplayed()
    }

    @Test
    fun genreChip_performsClickAction() {
        var clicked = false
        composeTestRule.setContent {
            GenreChip(
                genre = Genre(id = 1, name = "Action"),
                isSelected = true,
                onClick = {
                    clicked = true
                }
            )
        }
        composeTestRule.onNodeWithTag(GENRE_CHIP).performClick()
        assertTrue(clicked)
    }

    @Test
    fun genreChip_selected_hasCorrectSemanticDescription() {
        composeTestRule.setContent {
            GenreChip(
                genre = Genre(id = 1, name = "Action"),
                isSelected = true,
                onClick = {}
            )
        }
        composeTestRule.onNodeWithTag(GENRE_CHIP).assertContentDescriptionEquals(GENRE_SELECTED)
    }

    @Test
    fun genreChip_notSelected_hasCorrectSemanticDescription() {
        composeTestRule.setContent {
            GenreChip(
                genre = Genre(id = 1, name = "Action"),
                isSelected = false,
                onClick = {}
            )
        }
        composeTestRule.onNodeWithTag(GENRE_CHIP).assertContentDescriptionEquals(GENRE_NOT_SELECTED)
    }

    @Test
    fun genreChip_displaysCorrectText(){
        composeTestRule.setContent {
            GenreChip(
                genre = Genre(id = 1, name = "Action"),
                isSelected = false,
                onClick = {}
            )
        }
        composeTestRule.onNodeWithText("Action").assertExists().assertIsDisplayed()
    }
}