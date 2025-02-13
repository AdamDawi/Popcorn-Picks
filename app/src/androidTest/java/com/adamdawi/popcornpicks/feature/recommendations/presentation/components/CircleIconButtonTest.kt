package com.adamdawi.popcornpicks.feature.recommendations.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.R
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class CircleIconButtonTest{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun circleIconButton_isDisplayed() {
        composeTestRule.setContent {
            CircleIconButton(
                icon = painterResource(R.drawable.heart_ic),
                onClick = {},
                color = Color.Red,
                enabled = true,
                contentDescription = "Sample Icon"
            )
        }
        composeTestRule.onNodeWithContentDescription("Sample Icon").assertExists().assertIsDisplayed()
    }

    @Test
    fun circleIconButton_enabled_performsClickAction() {
        var clicked = false

        composeTestRule.setContent {
            CircleIconButton(
                icon = painterResource(R.drawable.heart_ic),
                onClick = { clicked = true },
                color = Color.Red,
                enabled = true,
                contentDescription = "Sample Icon"
            )
        }

        composeTestRule.onNodeWithContentDescription("Sample Icon")
            .assertIsEnabled()
            .performClick()

        assertTrue(clicked)
    }

    @Test
    fun circleIconButton_disabled_doesNotPerformClickAction() {
        var clicked = false

        composeTestRule.setContent {
            CircleIconButton(
                icon = painterResource(R.drawable.heart_ic),
                onClick = { clicked = true },
                color = Color.Red,
                enabled = false,
                contentDescription = "Sample Icon"
            )
        }

        composeTestRule.onNodeWithContentDescription("Sample Icon")
            .performClick()

        assertFalse(clicked)
    }
}