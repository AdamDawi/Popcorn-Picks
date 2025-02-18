package com.adamdawi.popcornpicks.feature.recommendations.presentation

import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.RecommendationsViewModel
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

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