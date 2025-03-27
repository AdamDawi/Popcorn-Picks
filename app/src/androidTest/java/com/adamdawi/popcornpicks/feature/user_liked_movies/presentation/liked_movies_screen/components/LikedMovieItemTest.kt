package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen.components

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LIKED_MOVIE_ITEM
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.TEXT_CHIP
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LikedMovieItemTest {

    private val likedMovieItem = LikedMovie(
        id = 1,
        title = "Spiderman",
        poster = null,
        releaseDate = "2024-01-20",
        voteAverage = 7.8,
        genres = listOf(Genre(1, "Action")),
        nextPage = 1
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup(){

    }
    @Test
    fun likedMovieItem_visibility_displayed(){
        composeTestRule.setContent {
            LikedMovieItem(
                movie = likedMovieItem
            )
        }

        composeTestRule.onNodeWithTag(LIKED_MOVIE_ITEM).assertExists().assertIsDisplayed()
    }
    @Test
    fun likedMovieItem_movieTitleAndYearConcatenation_correctTextDisplayed() {
        composeTestRule.setContent {
            LikedMovieItem(
                movie = likedMovieItem
            )
        }

        composeTestRule.onNodeWithText("Spiderman (2024)").assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMovieItem_emptyGenres_onlyYearTextChipDisplayed() {
        composeTestRule.setContent {
            LikedMovieItem(
                movie = likedMovieItem.copy(genres = emptyList())
            )
        }

        composeTestRule.onAllNodesWithTag(TEXT_CHIP).assertCountEquals(1)
    }

    @Test
    fun likedMovieItem_oneGenre_correctTextDisplayed() {
        composeTestRule.setContent {
            LikedMovieItem(
                movie = likedMovieItem
            )
        }

        composeTestRule.onNodeWithText("Action").assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMovieItem_threeGenres_correctTextsDisplayed() {
        composeTestRule.setContent {
            LikedMovieItem(
                movie = likedMovieItem.copy(genres = listOf(Genre(1, "Action"), Genre(2, "Comedy"), Genre(3, "Documentary")))
            )
        }

        composeTestRule.onNodeWithText("Action").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Comedy").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithText("Documentary").assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMovieItem_voteAverage_correctAverageDisplayed() {
        composeTestRule.setContent {
            LikedMovieItem(
                movie = likedMovieItem
            )
        }

        composeTestRule.onNodeWithText("7.8").assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMovieItem_starIcon_displayed() {
        composeTestRule.setContent {
            LikedMovieItem(
                movie = likedMovieItem
            )
        }

        composeTestRule.onNodeWithContentDescription("Star").assertExists().assertIsDisplayed()
    }

    @Test
    fun likedMovieItem_year_correctYearDisplayed() {
        composeTestRule.setContent {
            LikedMovieItem(
                movie = likedMovieItem
            )
        }

        composeTestRule.onNodeWithText("2024").assertExists().assertIsDisplayed()
    }
}