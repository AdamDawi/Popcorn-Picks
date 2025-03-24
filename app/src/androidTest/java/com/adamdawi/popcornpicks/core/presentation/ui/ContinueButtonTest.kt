package com.adamdawi.popcornpicks.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.core.presentation.PopcornPicksButton
import com.adamdawi.popcornpicks.core.presentation.theme.Red
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class PopcornPicksButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun popcornPicksButton_visibility_displayed() {
        composeTestRule.setContent {
            PopcornPicksButton(
                onClick = {},
                enabled = true,
                text = "Continue"
            )
        }

        composeTestRule.onNodeWithText("Continue").assertExists().assertIsDisplayed()
    }

    @Test
    fun popcornPicksButton_enabled_performsClickAction() {
        var clicked = false
        composeTestRule.setContent {
            PopcornPicksButton(
                onClick = { clicked = true },
                enabled = true,
                text = "Continue"
            )
        }

        composeTestRule.onNodeWithText("Continue").performClick()
        assertTrue(clicked)
    }

    @Test
    fun popcornPicksButton_disabled_doesNotPerformClickAction() {
        var clicked = false
        composeTestRule.setContent {
            PopcornPicksButton(
                onClick = { clicked = true },
                enabled = false,
                text = "Continue"
            )
        }

        composeTestRule.onNodeWithText("Continue").performClick()
        assertFalse(clicked)
    }

    //Visual tests should be verified by screenshot tests
    @Test
    fun popcornPicksButton_enabled_hasCorrectBackgroundColor() {
        composeTestRule.setContent {
            PopcornPicksButton(
                onClick = {},
                enabled = true,
                text = "Continue"
            )
        }

        val button = composeTestRule.onNodeWithText("Continue").fetchSemanticsNode()
        assertTrue(hasBackground(button, Red, CircleShape))
    }

    @Test
    fun popcornPicksButton_disabled_hasCorrectBackgroundColor() {
        composeTestRule.setContent {
            PopcornPicksButton(
                onClick = {},
                enabled = false,
                text = "Continue"
            )
        }

        val button = composeTestRule.onNodeWithText("Continue").fetchSemanticsNode()
        assertTrue(hasBackground(button, Red.copy(alpha = 0.3f), CircleShape))
    }

    private fun hasBackground(node: SemanticsNode, color: Color, shape: Shape): Boolean {
        return node.layoutInfo.getModifierInfo().filter { modifierInfo ->
            modifierInfo.modifier == Modifier.background(color = color, shape = shape)
        }.size == 1
    }
}