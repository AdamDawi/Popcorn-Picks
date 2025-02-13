package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.CARD_WITH_ANIMATED_BORDER
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class CardWithAnimatedBorderTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun cardWithAnimatedBorder_isDisplayed() {
        composeTestRule.setContent {
            CardWithAnimatedBorder {  }
        }
        composeTestRule.onNodeWithTag(CARD_WITH_ANIMATED_BORDER).assertExists().assertIsDisplayed()
    }

    @Test
    fun cardWithAnimatedBorder_performsClickAction(){
        var clicked = false
        composeTestRule.setContent {
            CardWithAnimatedBorder(
                onCardClick = { clicked = true }
            ) {  }
        }
        composeTestRule.onNodeWithTag(CARD_WITH_ANIMATED_BORDER).performClick()
        assertTrue(clicked)
    }
}