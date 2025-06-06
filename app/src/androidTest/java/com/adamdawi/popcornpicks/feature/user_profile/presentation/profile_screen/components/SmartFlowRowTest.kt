package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.ICON_LABEL_CHIP
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.SMART_FLOW_ROW
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import org.junit.Rule
import org.junit.Test

class SmartFlowRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun smartFlowRow_notEmptySmartFlowRowContent_smartFlowRowIsDisplayed(){
        composeTestRule.setContent {
            PopcornPicksTheme {
                SmartFlowRow {
                    ImageLabelChip(imageId = R.drawable.heart_solid_ic, label = "Action")
                }
            }
        }
        composeTestRule.onNodeWithTag(SMART_FLOW_ROW).assertExists().assertIsDisplayed()
    }

    @Test
    fun smartFlowRow_emptySmartFlowRowContent_smartFlowRowIsNotDisplayed(){
        composeTestRule.setContent {
            PopcornPicksTheme {
                SmartFlowRow {  }
            }
        }
        composeTestRule.onNodeWithTag(SMART_FLOW_ROW).assertIsNotDisplayed()
    }

    @Test
    fun smartFlowRow_notEmptySmartFlowRowContent_displaysCorrectNumberOfItems() {
        composeTestRule.setContent {
            PopcornPicksTheme {
                SmartFlowRow {
                    ImageLabelChip(imageId = R.drawable.heart_solid_ic, label = "Action")
                    ImageLabelChip(imageId = R.drawable.heart_solid_ic, label = "Comedy")
                    ImageLabelChip(imageId = R.drawable.heart_solid_ic, label = "Thriller")
                    ImageLabelChip(imageId = R.drawable.heart_solid_ic, label = "Drama")
                    ImageLabelChip(imageId = R.drawable.heart_solid_ic, label = "Horror")
                }
            }
        }

        val iconLabelChips = composeTestRule.onAllNodesWithTag(ICON_LABEL_CHIP)
        iconLabelChips.assertCountEquals(5)
    }

    @Test
    fun smartFlowRow_emptySmartFlowRowContent_notDisplayAnyOfItems() {
        composeTestRule.setContent {
            PopcornPicksTheme {
                SmartFlowRow {}
            }
        }

        composeTestRule.onAllNodesWithTag(ICON_LABEL_CHIP).assertCountEquals(0)
    }
}