package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.IMAGE_WITH_ANIMATED_BORDER
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_ITEM
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.REGULAR_IMAGE
import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieItemTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var testMovie: Movie

    @Before
    fun setUp() {
        testMovie = Movie(
            id = 1,
            title = "Spiderman",
            poster = "/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg",
            releaseDate = "2020-04-02",
            voteAverage = 7.6,
            genres = listOf(
                Genre(id = 36, name = "History"),
                Genre(id = 27, name = "Horror"),
                Genre(id = 10402, name = "Music")
            )
        )

    }

    @Test
    fun movieItem_isDisplayed() {
        composeTestRule.setContent {
            MovieItem(
                movie = testMovie,
                isSelected = true,
                onClick = {}
            )
        }
        composeTestRule.onNodeWithTag(MOVIE_ITEM).assertExists().assertIsDisplayed()
    }

    @Test
    fun movieItem_displaysCorrectTitle(){
        composeTestRule.setContent {
            MovieItem(
                movie = testMovie,
                isSelected = true,
                onClick = {}
            )
        }
        composeTestRule.onNodeWithText("Spiderman").assertExists().assertIsDisplayed()
    }

    @Test
    fun movieItem_yearIsNotEmpty_displaysCorrectYear(){
        composeTestRule.setContent {
            MovieItem(
                movie = testMovie,
                isSelected = true,
                onClick = {}
            )
        }
        composeTestRule.onNodeWithText("2020").assertExists().assertIsDisplayed()
    }

    @Test
    fun movieItem_yearIsEmpty_displaysPlaceholderString(){
        composeTestRule.setContent {
            MovieItem(
                movie = testMovie.copy(
                    releaseDate = ""
                ),
                isSelected = true,
                onClick = {}
            )
        }
        composeTestRule.onNodeWithText("???").assertExists().assertIsDisplayed()
    }

    @Test
    fun movieItem_performsClickAction(){
        var clicked = false
        composeTestRule.setContent {
            MovieItem(
                movie = testMovie,
                isSelected = true,
                onClick = { clicked = true }
            )
        }
        composeTestRule.onNodeWithTag(MOVIE_ITEM).performClick()
        assertTrue(clicked)
    }

    @Test
    fun movieItem_notSelected_displaysRegularImage() {
        composeTestRule.setContent {
            MovieItem(
                movie = testMovie,
                isSelected = false,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithTag(REGULAR_IMAGE).assertExists().assertIsDisplayed()
    }

    // This test might be flaky because the animated border appears only after the image is displayed.
    // However, waiting for the image to load is the only way to verify the animated border.
    @Test
    fun movieItem_selected_displaysImageWithAnimatedBorder() {
        composeTestRule.setContent {
            MovieItem(
                movie = testMovie,
                isSelected = true,
                onClick = {}
            )
        }
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag(IMAGE_WITH_ANIMATED_BORDER)
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithTag(IMAGE_WITH_ANIMATED_BORDER).assertExists().assertIsDisplayed()
    }
}