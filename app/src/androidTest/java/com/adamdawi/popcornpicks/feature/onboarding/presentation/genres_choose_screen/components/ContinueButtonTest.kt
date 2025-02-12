package com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen.components

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
import com.adamdawi.popcornpicks.core.presentation.theme.Red
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class ContinueButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun continueButton_visibility_displayed() {
        composeTestRule.setContent {
            ContinueButton(
                onClick = {},
                enabled = true
            )
        }

        composeTestRule.onNodeWithText("Continue").assertExists().assertIsDisplayed()
    }

    @Test
    fun continueButton_enabled_performsClickAction() {
        var clicked = false
        composeTestRule.setContent {
            ContinueButton(
                onClick = { clicked = true },
                enabled = true
            )
        }

        composeTestRule.onNodeWithText("Continue").performClick()
        assertTrue(clicked)
    }

    @Test
    fun continueButton_disabled_doesNotPerformClickAction() {
        var clicked = false
        composeTestRule.setContent {
            ContinueButton(
                onClick = { clicked = true },
                enabled = false
            )
        }

        composeTestRule.onNodeWithText("Continue").performClick()
        assertFalse(clicked)
    }

    //Visual tests should be verified by screenshot tests
    @Test
    fun continueButton_enabled_hasCorrectBackgroundColor() {
        composeTestRule.setContent {
            ContinueButton(
                onClick = {},
                enabled = true
            )
        }

        val button = composeTestRule.onNodeWithText("Continue").fetchSemanticsNode()
        assertTrue(hasBackground(button, Red, CircleShape))
    }

    @Test
    fun continueButton_disabled_hasCorrectBackgroundColor() {
        composeTestRule.setContent {
            ContinueButton(
                onClick = {},
                enabled = false
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