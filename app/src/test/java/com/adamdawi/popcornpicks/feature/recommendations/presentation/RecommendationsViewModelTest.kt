package com.adamdawi.popcornpicks.feature.recommendations.presentation

import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.MoviesByGenreRepository
import com.adamdawi.popcornpicks.feature.recommendations.domain.RecommendedMovie
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.RecommendationsRepository
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecommendationsViewModelTest {

    private lateinit var sut: RecommendationsViewModel
    private lateinit var moviesByGenreRepository: MoviesByGenreRepository
    private lateinit var recommendationsRepository: RecommendationsRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setup(){
        moviesByGenreRepository = mockk()
        recommendationsRepository = mockk()
    }

    //INIT
    @Test
    fun init_recommendationsState_isMovieLikedStateIsFalse(){
        //Arrange

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Assert
        assertThat(sut.state.value.isMovieLiked, `is`(false))
    }

    @Test
    fun init_recommendationsState_isMovieScratchedStateIsFalse(){
        //Arrange

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(false))
    }

    @Test
    fun init_recommendationsState_recommendedMovieStateIsEmpty(){
        //Arrange
        val emptyRecommendedMovie = RecommendedMovie(0, "", "", "", 0.0, emptyList())
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Act

        //Assert
        assertThat(sut.state.value.recommendedMovie, `is`(emptyRecommendedMovie))
    }


    @Test
    fun init_recommendationsState_recommendedMoviesStateIsEmptyList(){
        //Arrange

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Assert
        assertThat(sut.state.value.recommendedMovies, `is`(emptyList()))
    }

    //ON IMAGE SCRATCHED
    @Test
    fun onImageScratched_imageIsScratched_isMovieScratchedStateUpdatedToTrue(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Act
        sut.onAction(RecommendationsAction.OnImageScratched)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(true))
    }

    @Test
    fun onImageScratched_imageIsNotScratched_isMovieScratchedStateUpdatedToTrue(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Act
        sut.onAction(RecommendationsAction.OnImageScratched)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(true))
    }
    //ON HEART CLICKED
    @Test
    fun onHeartClicked_movieNotLiked_isMovieLikedStateUpdatedToTrue(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Act
        sut.onAction(RecommendationsAction.OnHeartClicked)

        //Assert
        assertThat(sut.state.value.isMovieLiked, `is`(true))
    }


    @Test
    fun onHeartClicked_movieLiked_isMovieLikedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Act
        sut.onAction(RecommendationsAction.OnHeartClicked)
        sut.onAction(RecommendationsAction.OnHeartClicked)

        //Assert
        assertThat(sut.state.value.isMovieLiked, `is`(false))
    }
    //add movie to watch later db
    //1. success
    //- invoked once with correct movie
    //- set isFavourite state to true

    //2. error
    //- invoked once with correct movie
    //- isFavourite state is false
    //- event Channel Updated With Correct Error

    //reroll
    //- set isFavourite state to false
    //- set isMovieScratched state to false
    //- recommendedMovie state updated with new movie
    //- if recommendedMovies state is empty - fetch movies

    @Test
    fun onRerollClicked_movieNotLiked_isMovieLikedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Act
        sut.onAction(RecommendationsAction.OnRerollClicked)

        //Assert
        assertThat(sut.state.value.isMovieLiked, `is`(false))
    }

    @Test
    fun onRerollClicked_movieLiked_isMovieLikedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)
        sut.onAction(RecommendationsAction.OnHeartClicked)

        //Act
        sut.onAction(RecommendationsAction.OnRerollClicked)

        //Assert
        assertThat(sut.state.value.isMovieLiked, `is`(false))
    }

    @Test
    fun onRerollClicked_movieNotScratched_isMovieScratchedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)

        //Act
        sut.onAction(RecommendationsAction.OnRerollClicked)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(false))
    }


    @Test
    fun onRerollClicked_movieScratched_isMovieScratchedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository)
        sut.onAction(RecommendationsAction.OnImageScratched)

        //Act
        sut.onAction(RecommendationsAction.OnRerollClicked)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(false))
    }
    //fetch movies
    //1. success
    //- recommendedMovies state updated with correct data
    //- repository get movies recommendations invoked correct number of times
    //- error state is not updated
    //- loading state is set to true while fetching
    //- loading state is set to false after fetching
    //2. error
    //- recommendedMovies state is not updated
    //- repository get movies recommendations invoked correct number of times
    //- error state is updated with correct error
    //- loading state is set to true while fetching
    //- loading state is set to false after fetching


}