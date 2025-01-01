package com.adamdawi.popcornpicks.feature.recommendations.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyMovie
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.domain.repository.MoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.MoviesByGenreRepository
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.RecommendationsRepository
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecommendationsViewModelTest {

    private lateinit var sut: RecommendationsViewModel
    private lateinit var moviesByGenreRepository: MoviesByGenreRepository
    private lateinit var recommendationsRepository: RecommendationsRepository
    private lateinit var moviesDbRepository: MoviesDbRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setup(){
        moviesByGenreRepository = mockk()
        recommendationsRepository = mockk()
        moviesDbRepository = mockk()
    }

    //INIT
    @Test
    fun init_recommendationsState_isMovieLikedStateIsFalse(){
        //Arrange

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.isMovieLiked, `is`(false))
    }

    @Test
    fun init_recommendationsState_isMovieScratchedStateIsFalse(){
        //Arrange

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(false))
    }

    @Test
    fun init_recommendationsState_recommendedMovieStateIsEmpty(){
        //Arrange
        val emptyRecommendedMovie = Movie(0, "", "", "", 0.0, emptyList())
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Act

        //Assert
        assertThat(sut.state.value.recommendedMovie, `is`(emptyRecommendedMovie))
    }


    @Test
    fun init_recommendationsState_recommendedMoviesStateIsEmptyList(){
        //Arrange

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.recommendedMovies, `is`(emptyList()))
    }

    //ON IMAGE SCRATCHED
    @Test
    fun onImageScratched_imageIsScratched_isMovieScratchedStateUpdatedToTrue(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Act
        sut.onAction(RecommendationsAction.OnImageScratched)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(true))
    }

    @Test
    fun onImageScratched_imageIsNotScratched_isMovieScratchedStateUpdatedToTrue(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Act
        sut.onAction(RecommendationsAction.OnImageScratched)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(true))
    }

    //ON HEART CLICKED
    @Test
    fun onHeartClicked_movieNotLiked_isMovieLikedStateUpdatedToTrue(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Act
        sut.onAction(RecommendationsAction.OnHeartClicked)

        //Assert
        assertThat(sut.state.value.isMovieLiked, `is`(true))
    }


    @Test
    fun onHeartClicked_movieLiked_isMovieLikedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

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
    //- recommendedMovie state updated with new movie
    //- if recommendedMovies state is empty - fetch movies

    @Test
    fun onRerollClicked_movieNotLiked_isMovieLikedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Act
        sut.onAction(RecommendationsAction.OnRerollClicked)

        //Assert
        assertThat(sut.state.value.isMovieLiked, `is`(false))
    }

    @Test
    fun onRerollClicked_movieLiked_isMovieLikedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)
        sut.onAction(RecommendationsAction.OnHeartClicked)

        //Act
        sut.onAction(RecommendationsAction.OnRerollClicked)

        //Assert
        assertThat(sut.state.value.isMovieLiked, `is`(false))
    }

    @Test
    fun onRerollClicked_movieNotScratched_isMovieScratchedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Act
        sut.onAction(RecommendationsAction.OnRerollClicked)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(false))
    }


    @Test
    fun onRerollClicked_movieScratched_isMovieScratchedStateUpdatedToFalse(){
        //Arrange
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)
        sut.onAction(RecommendationsAction.OnImageScratched)

        //Act
        sut.onAction(RecommendationsAction.OnRerollClicked)

        //Assert
        assertThat(sut.state.value.isMovieScratched, `is`(false))
    }

    //FETCH RECOMMENDED MOVIES

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

    @Test
    fun fetchRecommendedMovies_successFromDbWithNotEmptyList_likedMoviesStateUpdatedWithCorrectMovies(){
        //Arrange
        val listOfMovies = listOf(
            Movie(
                id = 1,
                title = "Spiderman",
                poster = "/1g0dhYtq4irTY",
                releaseDate = "2020-04-02",
                voteAverage = 2.3,
                genres = listOf(Genre(2, "Action"), Genre(3, "Adventure"))
            ),
            Movie(
                id = 1,
                title = "Hulk",
                poster = "/1g0dhYtq4irTY",
                releaseDate = "2020-06-02",
                voteAverage = 5.0,
                genres = listOf(Genre(2, "Action"), Genre(6, "Comedy"))
            )
        )
        coEvery { moviesDbRepository.getMovies() } answers {
            Result.Success(listOfMovies)
        }

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.likedMovies, `is`(listOfMovies))
    }

    @Test
    fun fetchRecommendedMovies_successFromDbWithEmptyList_likedMoviesStateUpdatedWithEmptyList(){
        //Arrange
        coEvery { moviesDbRepository.getMovies() } answers {
            Result.Success(emptyList())
        }

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.likedMovies, `is`(emptyList()))
    }

    @Test
    fun fetchRecommendedMovies_errorFromDb_likedMoviesStateNotUpdated(){
        //Arrange
        coEvery { moviesDbRepository.getMovies() } answers {
            Result.Error(DataError.Local.DISK_FULL)
        }

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.likedMovies, `is`(emptyList()))
    }

    @Test
    fun fetchRecommendedMovies_successFromDbWithNotEmptyListAndSuccessFromApi_recommendedMoviesStateUpdatedWithCorrectMovies(){
        //Arrange
        val listOfMoviesDb = listOf(
            Movie(
                id = 1,
                title = "Thor",
                poster = "/1g0dhYtq4irTY",
                releaseDate = "2020-04-02",
                voteAverage = 2.3,
                genres = listOf(Genre(2, "Action"), Genre(3, "Adventure"))
            ),
            Movie(
                id = 1,
                title = "Hulk",
                poster = "/1g0dhYtq4irTY",
                releaseDate = "2020-06-02",
                voteAverage = 5.0,
                genres = listOf(Genre(2, "Action"), Genre(6, "Comedy"))
            )
        )
        val recommendedMovies = listOf(dummyMovie)
        coEvery { moviesDbRepository.getMovies() } answers {
            Result.Success(listOfMoviesDb)
        }
        coEvery { recommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(recommendedMovies)
        }

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.recommendedMovies, `is`(recommendedMovies))
    }

    @Test
    fun fetchRecommendedMovies_success_loadingStateIsSetToTrueWhileFetching(){
        //Arrange
        coEvery { moviesDbRepository.getMovies() } coAnswers {
            delay(1000)
            Result.Success(dummyMovieList)
        }
        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(true))
    }

    @Test
    fun fetchRecommendedMovies_success_loadingStateIsSetToFalseAfterFetching(){
        //Arrange
        coEvery { moviesDbRepository.getMovies() } answers  {
            Result.Success(dummyMovieList)
        }

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.isLoading, `is`(false))
    }

    @Test
    fun fetchRecommendedMoviesFromApi_success_recommendedMoviesStateUpdatedWithCorrectMovies(){
        //Arrange
        val listOfMoviesDb = listOf(
            Movie(
                id = 1,
                title = "Thor",
                poster = "/1g0dhYtq4irTY",
                releaseDate = "2020-04-02",
                voteAverage = 2.3,
                genres = listOf(Genre(2, "Action"), Genre(3, "Adventure"))
            ),
            Movie(
                id = 1,
                title = "Hulk",
                poster = "/1g0dhYtq4irTY",
                releaseDate = "2020-06-02",
                voteAverage = 5.0,
                genres = listOf(Genre(2, "Action"), Genre(6, "Comedy"))
            )
        )
        val recommendedMovies = listOf(dummyMovie)
        coEvery { moviesDbRepository.getMovies() } answers {
            Result.Success(listOfMoviesDb)
        }
        coEvery { recommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Success(recommendedMovies)
        }

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.recommendedMovies, `is`(recommendedMovies))
    }

    @Test
    fun fetchRecommendedMoviesFromApi_error_errorStateUpdatedWithCorrectError() = runTest{
        //Arrange
        val listOfMoviesDb = listOf(
            Movie(
                id = 1,
                title = "Thor",
                poster = "/1g0dhYtq4irTY",
                releaseDate = "2020-04-02",
                voteAverage = 2.3,
                genres = listOf(Genre(2, "Action"), Genre(3, "Adventure"))
            ),
            Movie(
                id = 1,
                title = "Hulk",
                poster = "/1g0dhYtq4irTY",
                releaseDate = "2020-06-02",
                voteAverage = 5.0,
                genres = listOf(Genre(2, "Action"), Genre(6, "Comedy"))
            )
        )
        coEvery { moviesDbRepository.getMovies() } answers {
            Result.Success(listOfMoviesDb)
        }
        coEvery { recommendationsRepository.getMoviesBasedOnMovie(any(), any()) } answers {
            Result.Error(DataError.Network.SERVER_ERROR)
        }

        //Act
        sut = RecommendationsViewModel(moviesByGenreRepository, recommendationsRepository, moviesDbRepository, replaceMainDispatcherRule.testDispatcher)

        //Assert
        assertThat(sut.state.value.error, `is`(DataError.Network.SERVER_ERROR.asUiText()))
    }
}