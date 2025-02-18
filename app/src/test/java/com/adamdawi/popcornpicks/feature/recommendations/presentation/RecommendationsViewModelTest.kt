package com.adamdawi.popcornpicks.feature.recommendations.presentation

import com.adamdawi.popcornpicks.core.data.dummy.dummyMovie
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovieList
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.DataError
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.onboarding.domain.model.Genre
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.RecommendationsAction
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.RecommendationsViewModel
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
    private lateinit var remoteMovieRecommendationsRepository: RemoteMovieRecommendationsRepository
    private lateinit var likedMoviesDbRepository: LikedMoviesDbRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setup(){
        remoteMovieRecommendationsRepository = mockk()
        likedMoviesDbRepository = mockk()
    }
}