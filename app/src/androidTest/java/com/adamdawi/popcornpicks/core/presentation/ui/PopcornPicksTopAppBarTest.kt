package com.adamdawi.popcornpicks.core.presentation.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.TOP_APP_BAR_ACTIONS
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.TOP_APP_BAR_NAVIGATION_ICON
import org.junit.Rule
import org.junit.Test

class PopcornPicksTopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun popcornPicksTopAppBar_titleTextIsNull_titleTextIsNotDisplayed(){
        composeTestRule.setContent {
            PopcornPicksTopAppBar(
                titleText = null
            )
        }
        composeTestRule.onNodeWithText("", substring = true).assertIsNotDisplayed().assertDoesNotExist() //any text on screen
    }

    @Test
    fun popcornPicksTopAppBar_titleTextIsNotNull_correctTitleTextIsDisplayed(){
        composeTestRule.setContent {
            PopcornPicksTopAppBar(
                titleText = "Profile"
            )
        }
        composeTestRule.onNodeWithText("Profile").assertExists().assertIsDisplayed()
    }


    @Test
    fun popcornPicksTopAppBar_actionsIsNull_actionsIsNotDisplayed(){
        composeTestRule.setContent {
            PopcornPicksTopAppBar(
                actions = null
            )
        }
        composeTestRule.onNodeWithTag(TOP_APP_BAR_ACTIONS).assertIsNotDisplayed().assertDoesNotExist()
    }

    @Test
    fun popcornPicksTopAppBar_actionsIsNotNull_actionsIsDisplayed(){
        composeTestRule.setContent {
            PopcornPicksTopAppBar(
                actions = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                }
            )
        }
        composeTestRule.onNodeWithTag(TOP_APP_BAR_ACTIONS).assertExists().assertIsDisplayed()
    }

    @Test
    fun popcornPicksTopAppBar_navigationIconIsNull_navigationIconIsNotDisplayed(){
        composeTestRule.setContent {
            PopcornPicksTopAppBar(
                navigationIcon = null
            )
        }
        composeTestRule.onNodeWithTag(TOP_APP_BAR_NAVIGATION_ICON).assertIsNotDisplayed().assertDoesNotExist()
    }

    @Test
    fun popcornPicksTopAppBar_navigationIconIsNotNull_navigationIconIsDisplayed(){
        composeTestRule.setContent {
            PopcornPicksTopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            )
        }
        composeTestRule.onNodeWithTag(TOP_APP_BAR_NAVIGATION_ICON).assertExists().assertIsDisplayed()
    }
}